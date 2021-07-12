package 设计模式.备份模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description 储存备份对象
 */
public class Storage {

    private Memento memento;

    public Storage(Memento memento) {
        this.memento = memento;
    }

    public Memento getMemento() {
        return memento;
    }

    public void setMemento(Memento memento) {
        this.memento = memento;
    }
}
