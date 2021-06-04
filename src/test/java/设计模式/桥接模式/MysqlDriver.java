package 设计模式.桥接模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description 数据库厂商实现
 */
public class MysqlDriver implements DriverManager {

    @Override
    public String getConnection() {
        return "Mysql驱动";
    }

}
