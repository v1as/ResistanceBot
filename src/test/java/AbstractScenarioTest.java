import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.bots.AbsSender;
import ru.v1as.action.ActionProcessor;
import ru.v1as.callbacks.CallbackProcessor;
import ru.v1as.model.Storage;

/**
 * Created by ivlasishen
 * on 18.04.2017.
 */
public class AbstractScenarioTest {

    private ActionProcessor processor = new ActionProcessor(Mockito.mock(AbsSender.class));
    private Storage storage = new Storage();
    private CallbackProcessor callbackHandler = new CallbackProcessor(storage, processor);

    @Test
    public void test12() throws Exception {

    }
}
