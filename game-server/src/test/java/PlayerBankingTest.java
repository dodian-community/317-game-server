import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Bank;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class PlayerBankingTest {

    private Player player = mock(Player.class);

    @Test
    public void Test_Deposit_50_Then_Withdraw_20() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        player = player.getClass().getConstructor().newInstance();

        player.getInventory().resetItems();
        player.clearBank();

        player.getInventory().add(995, 1000);
        Bank.deposit(player, 995, 1, 50);
        Bank.withdraw(player, 995, 1, 20, 0);
        assertEquals(20, player.getInventory().get(1).getAmount());
    }

    @Test
    public void Test_Deposit_1050_When_We_Only_Have_1000() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        player = player.getClass().getConstructor().newInstance();

        player.getInventory().resetItems();
        player.clearBank();

        player.getInventory().add(995, 1000);
        Bank.deposit(player, 995, 1, 1050);
        assertEquals(0, player.getInventory().get(1).getAmount());
        assertEquals(1000, player.getBank(player.getCurrentBankTab()).getAmount(995));
    }
}
