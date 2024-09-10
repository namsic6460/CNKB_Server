package lkd.namsic.cnkb.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lkd.namsic.cnkb.dto.KakaoExtra;
import lkd.namsic.cnkb.dto.KakaoMessage;
import lkd.namsic.cnkb.dto.KakaoUserInfo;
import lkd.namsic.cnkb.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdbClient {

    private final String adbAddress = "192.168.0.150:5555";

    private final AtomicBoolean usingWriter = new AtomicBoolean(false);
    private final AtomicBoolean usingReader = new AtomicBoolean(false);
    public final Queue<String[]> waitingMessageQueue = new ConcurrentLinkedQueue<>();

    private final ObjectMapper objectMapper;
    private final KakaoDecrypt kakaoDecrypt;
    private final MessageService messageService;

    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Thread readThread;

    private Long lastLog;

    @PostConstruct
    public void postConstruct() {
        this.initialize();

        this.readThread = new Thread(() -> {
            String line;
            while(true) {
                try {
                    line = this.read();
                    if (line == null || this.usingWriter.get()) {
                        //noinspection BusyWait
                        Thread.sleep(10);
                        continue;
                    }

                    this.usingReader.set(true);
                    String[] split = line.split("\\|");
                    this.processMessageSplit(split);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                } finally {
                    this.usingReader.set(false);
                }
            }
        });
        this.readThread.start();
    }

    private void processMessageSplit(String[] split) throws JsonProcessingException {
        this.lastLog = Long.parseLong(split[0]);
        long chatId = Long.parseLong(split[3]);
        long userId = Long.parseLong(split[4]);
        String message = split[5];
        KakaoExtra extra = this.objectMapper.readValue(split[13], KakaoExtra.class);

        if (BooleanUtils.isTrue(extra.isMine())) {
            return;
        }

        String decryptedMessage = this.kakaoDecrypt.decrypt(extra.enc(), message, userId);
        KakaoUserInfo userInfo = this.kakaoDecrypt.getUserInfo(this, chatId, userId);
        this.messageService.processMessage(new KakaoMessage(decryptedMessage, userInfo));

        String[] waitingMessage;
        while((waitingMessage = this.waitingMessageQueue.poll()) != null) {
            this.processMessageSplit(waitingMessage);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            if (this.writer != null) {
                this.writer.close();
            }
        } catch (IOException e) {
            log.error("Failed to close writer", e);
            System.exit(-1);
        }

        try {
            if (this.reader != null) {
                this.reader.close();
            }
        } catch (IOException e) {
            log.error("Failed to close reader", e);
        }

        if (this.readThread != null && this.readThread.isAlive()) {
            this.readThread.interrupt();
        }

        if (this.process != null) {
            this.process.destroy();
        }

        try {
            ProcessBuilder appStopper = new ProcessBuilder("adb.exe", "-s", this.adbAddress, "shell", "am", "force-stop", "lkd.namsic.noti");
            appStopper.start();
        } catch (IOException e) {
            log.error("Failed to stop android app");
        }
    }

    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    public void readDb() {
        if (this.usingWriter.get() || this.usingReader.get()) {
            return;
        }

        this.usingWriter.set(true);

        try {
            this.write("SELECT * FROM chat_logs WHERE _id > " + this.lastLog + " ORDER BY _id ASC;");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            this.usingWriter.set(false);
        }
    }

    private void initialize() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("adb.exe", "-s", this.adbAddress, "shell", "su", "-c", "sqlite3", "/data/data/com.kakao.talk/databases/KakaoTalk.db");
            this.process = processBuilder.redirectErrorStream(true).start();
            this.writer = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));

            this.write("ATTACH DATABASE \"/data/data/com.kakao.talk/databases/KakaoTalk2.db\" AS db2;");
            this.write("SELECT _id FROM chat_logs ORDER BY _id DESC LIMIT 1;");

            String result = this.reader.readLine();
            this.lastLog = Long.parseLong(result);

            ProcessBuilder appRestartBuilder = new ProcessBuilder(
                "adb.exe", "-s", this.adbAddress, "shell", "am", "force-stop", "lkd.namsic.noti", "&&",
                "am", "start", "-n", "lkd.namsic.noti/lkd.namsic.noti.MainActivity");
            Process appRestarter = appRestartBuilder.redirectErrorStream(true).start();

            BufferedReader restartReader = new BufferedReader(new InputStreamReader(appRestarter.getInputStream()));
            log.info(restartReader.readLine());

            restartReader.close();
            appRestarter.destroy();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void write(String s) {
        try {
            this.writer.write(s + "\n");
            this.writer.flush();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String read() {
        try {
            return this.reader.readLine();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
