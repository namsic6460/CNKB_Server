package lkd.namsic.cnkb.handler.mine;

import lkd.namsic.cnkb.constant.MineConstants;
import lkd.namsic.cnkb.domain.user.Miner;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.MinerRepository;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.exception.ReplyException;
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
                // TODO
            }
        }

        throw new ReplyException();
    }

    private HandleResult getMinerCheckResult(User user, Miner miner) {
        int index = miner.getLv() - 1;
        int gatherDelay = MineConstants.MINER_GATHER_DELAY.get(index);
        long gatheredCount = ChronoUnit.SECONDS.between(miner.getCheckedAt(), LocalDateTime.now()) / gatherDelay;

        if (gatheredCount == 0) {
            throw new ReplyException("획득한 광석이 없습니다");
        }

        ItemType itemType = MineConstants.MINER_ITEMS.get(index);
        this.minerRepository.updateCheckedAt(miner, miner.getCheckedAt().plusSeconds(gatheredCount * gatherDelay));

        gatheredCount = Math.min(gatheredCount, MineConstants.MINER_MAX_STORAGE_COUNT.get(index));
        int currentCount = this.inventoryService.addItem(user, itemType, (int) gatheredCount);
        return HandleResult.itemGathered(itemType, (int) gatheredCount, currentCount);
    }
}
