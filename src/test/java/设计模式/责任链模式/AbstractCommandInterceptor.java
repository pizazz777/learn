package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/02
 * @description
 */
public abstract class AbstractCommandInterceptor implements CommandInterceptor {


    protected CommandInterceptor next;


    @Override
    public CommandInterceptor getNext() {
        return next;
    }


    @Override
    public void setNext(CommandInterceptor next) {
        this.next = next;
    }
}
