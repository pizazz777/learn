package 设计模式.备份模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description
 */
public class OriginalTest {

    public static void main(String[] args) {
        Original original = new Original("张三");
        Storage storage = new Storage(original.createMemento());
        System.out.println("原来的值: " + original.getValue());

        original.setValue("李四");
        System.out.println("修改后的值: " + original.getValue());

        original.restoreMemento(storage.getMemento());
        System.out.println("还原的值: " + original.getValue());

        original.setValue("李四");
        storage = new Storage(original.createMemento());
        original.setValue("王五");
        System.out.println("修改后的值: " + original.getValue());

        original.restoreMemento(storage.getMemento());
        System.out.println("还原的值: " + original.getValue());
    }

}
