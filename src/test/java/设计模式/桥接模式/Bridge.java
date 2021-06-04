package 设计模式.桥接模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class Bridge {

    private DriverManager driverManager;

    public void setDriverManager(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    public void connection() {
        System.out.println("现在是什么驱动: " + driverManager.getConnection());
    }


}
