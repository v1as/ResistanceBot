import com.google.common.collect.Lists;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import ru.v1as.ResistanceBot;
import ru.v1as.action.ActionProcessor;
import ru.v1as.command.StartGameCommand;
import ru.v1as.model.Game;
import ru.v1as.model.Storage;
import ru.v1as.test.UserFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.v1as.test.UpdateFactory.*;

/**
 * Created by ivlasishen
 * on 18.04.2017.
 */
public class AbstractScenarioTest {

    private static Integer a = 0;
    private UserFactory userFactory = new UserFactory();
    private ActionProcessor processor = new ActionProcessor();
    private ResistanceBot resistanceBot = new ResistanceBot(processor);
    private User user1;
    private User user2;
    private User user3;
    private List<User> users;
    private Storage<Game> storage;

    {
        processor.setSender(Mockito.mock(AbsSender.class));
        storage = resistanceBot.getStorage();
        user1 = userFactory.build("user1");
        user2 = userFactory.build("user2");
        user3 = userFactory.build("user3");
        users = Lists.newArrayList(user1, user2, user3);
    }

    @Test
    public void test() throws Exception {
        users.forEach(u -> send(privateMessage(u, "hi")));
        assertEquals(3, storage.getUserId2ChatId().size());
        assertEquals(0, storage.getSessions().size());
        send(command(publicMessage(user1, StartGameCommand.START_GAME)));
        assertEquals(1, storage.getSessions().size());
        users.forEach(u -> send(command(publicMessage(u, "/join"))));
    }


    protected void send(Update update) {
        this.resistanceBot.onUpdateReceived(update);
    }

}
