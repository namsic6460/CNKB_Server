package lkd.namsic.cnkb.handler.mine;

import lkd.namsic.cnkb.constant.MineConstants;
import lkd.namsic.cnkb.domain.user.Miner;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.MinerRepository;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.enums.MinerStat;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MinerHandler extends AbstractHandler {

    private final MinerRepository minerRepository;
    private final InventoryService inventoryService;

    @Override
    public List<String> getRootCommands() {
        return List.of("채굴기", "채굴", "miner");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        this.checkUser(userData);
    }

    @Nullable
    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        User user = userData.getUser();
        Miner miner = user.getMiner();

        switch (commands.size()) {
            case 1 -> {
                return this.getMinerCheckResult(user, miner);
            }

            case 2 -> {
                return switch (commands.get(1).toLowerCase()) {
                    case "레벨", "lv" -> this.getMinerLvInfo(miner);
                    default -> throw new UserReplyException();
                };
            }

            case 3 -> {
                return switch (commands.get(1).toLowerCase()) {
                    case "upgrade", "업그레이드", "강화" -> this.upgradeMinerStat(user, miner, commands.get(2).toLowerCase());
                    default -> throw new UserReplyException();
                };
            }
        }

        throw new UserReplyException();
    }

    private HandleResult getMinerCheckResult(User user, Miner miner) {
        int gatherDelay = MineConstants.MINER_GATHER_DELAY.get(miner.getSpeedLv() - 1);
        long gatheredCount = ChronoUnit.SECONDS.between(miner.getCheckedAt(), LocalDateTime.now()) / gatherDelay;

        if (gatheredCount == 0) {
            throw new UserReplyException("획득한 광석이 없습니다");
        }

        ItemType itemType = MineConstants.MINER_ITEMS.get(miner.getQualityLv() - 1);
        this.minerRepository.updateCheckedAt(miner, miner.getCheckedAt().plusSeconds(gatheredCount * gatherDelay));

        gatheredCount = Math.min(gatheredCount, MineConstants.MINER_MAX_STORAGE_COUNT.get(miner.getStorageLv() - 1));
        int currentCount = this.inventoryService.addItem(user, itemType, (int) gatheredCount);
        return HandleResult.itemGathered(itemType, (int) gatheredCount, currentCount);
    }

    private HandleResult getMinerLvInfo(Miner miner) {
        return new HandleResult("[채굴기 레벨]\n- 속도 레벨: " + miner.getSpeedLv() +
            "Lv\n- 광석 레벨: " + miner.getQualityLv() + "Lv\n- 저장고 레벨: " + miner.getStorageLv() + "Lv");
    }

    private HandleResult upgradeMinerStat(User user, Miner miner, String minerStatName) {
        MinerStat minerStat = MinerStat.find(minerStatName);

        int currentLv = MinerStat.getMinerStatValue(miner, minerStat);

        long money = user.getMoney();
        long requiredMoneyToNext = MinerStat.getRequiredMoney(minerStat, currentLv);

        if (money < requiredMoneyToNext) {
            throw new UserReplyException("돈이 부족합니다. (필요 돈 : " + money + ")");
        }

        if (minerStat.isMaxLv(currentLv)) {
            throw new UserReplyException("더이상 강화할 수 없습니다. (Lv : " + currentLv + ")");
        }

        currentLv++;

        switch (minerStat) {
            case SPEED -> minerRepository.updateSpeedLv(miner, currentLv);
            case QUALITY -> minerRepository.updateQualityLv(miner, currentLv);
            case STORAGE -> minerRepository.updateStorageLv(miner, currentLv);
        }

        return HandleResult.minerStatUpgraded(minerStat, currentLv);
    }
}
