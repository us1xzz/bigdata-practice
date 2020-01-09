package com.xzz.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.w3c.dom.UserDataHandler;

import java.io.IOException;

public class hbasetest {
    private static Connection connection = null;
    private static Admin admin = null;

    static {
        try {
            //获取文件配置信息
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", "10.12.18.202,10.12.18.203,10.12.18.204");
            //获取管理员对象
            connection = ConnectionFactory.createConnection(configuration);

            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //1.判断表是否存在
    public static boolean isTableExist(String tableName) throws IOException {
//        //获取文件配置信息
//        //HBaseConfiguration configuration = new HBaseConfiguration();
//        Configuration configuration = HBaseConfiguration.create();
//        configuration.set("hbase.zookeeper.quorum","10.12.18.202,10.12.18.203,10.12.18.204");
//        //获取管理员对象
//        //HBaseAdmin admin = new HBaseAdmin(configuration);
//        Connection connection = ConnectionFactory.createConnection(configuration);
//        Admin admin = connection.getAdmin();
//        //判断其是否存在
        boolean exists = admin.tableExists(TableName.valueOf(tableName));
        //admin.close();
        return exists;
    }

    //2.创建表
    public static void createTable(String tableName, String... cfs) throws IOException {

        //1.判断是否存在列族信息
        if (cfs.length < 0) {
            System.out.println("请设置列族信息");
        }
        //2.判断表是否存在
        if (isTableExist(tableName)) {
            System.out.println(tableName + "表已存在");
        } else {
            //3.创建表描述器
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            //4.循环添加列族信息
            for (String cf : cfs) {
                //5.创建列族描述器
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
                //6.添加列族信息
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            //3.创建表
            admin.createTable(hTableDescriptor);
        }

    }

    //3.删除表
    public static void dropTable(String tableName) throws IOException {
        //1.判断表是否存在
        if (!isTableExist(tableName)) {
            System.out.println(tableName + "表不存在！！！");
        }
        //2.使表下线
        admin.disableTable(TableName.valueOf(tableName));
        //3.删除表
        admin.deleteTable(TableName.valueOf(tableName));
    }
    //4.创建命名空间


    //5.向表中插入数据
    public static void putData(String tableName, String rowkey, String cf, String cn, String value) throws IOException {

        //1.获取表对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //2.创建普通对象
        Put put = new Put(Bytes.toBytes(rowkey));
        //3.给put对象赋值
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cn), Bytes.toBytes(value));
        //4.插入数据
        table.put(put);
        //5.关闭资源
        table.close();
    }

    //6.获取数据（get）
    public static void getData(String tableName, String rowKey, String cf, String cn) throws IOException {
        //1.获取表对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //2.创建get对象
        Get get = new Get(Bytes.toBytes(rowKey));
        //2.1指定获取的列族
        get.addFamily(Bytes.toBytes(cf));
        //2.2指定列族和列
        get.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cn));
        //2.3设置获取数据的版本数
        get.setMaxVersions(5);

        //3.获取数据
        Result result = table.get(get);
        //4.解析result并打印
        for (Cell cell : result.rawCells()) {
            //5.打印数据
            System.out.println("CF:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
                    ",CN:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                    ",values:" + Bytes.toString(CellUtil.cloneValue(cell)));
        }

        //6.关闭表连接
        table.close();
    }

    //7.获取数据（scan）
    public static void scanTable(String tableName) throws IOException {
        //1.获取表对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //2.构建SCan对象
        Scan scan = new Scan();
        //3.扫描全表
        ResultScanner resultScanner = table.getScanner(scan);
        //4.解析resultScanner
        for (Result result : resultScanner) {
            //5.解析result并打印
            for (Cell cell : result.rawCells()) {
                System.out.println("CF:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
                        ",CN:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                        ",values:" + Bytes.toString(CellUtil.cloneValue(cell)));
            }

        }
        //7.关闭表连接
        table.close();
    }

    //8.删除数据
    public static void deleteData(String tableName, String rowkey, String cf, String cn) throws IOException {
        //1.获取table对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //2.构建删除对象
        Delete delete = new Delete(Bytes.toBytes(rowkey));
        //2.1 设置删除的列，建议用这个addColumns(),不用addColumn()
        delete.addColumns(Bytes.toBytes(cf), Bytes.toBytes(cn), 1574405809325L);
        //2.2 删除指定的列族
        delete.addFamily(Bytes.toBytes(cf));


        //3.执行删除操作
        table.delete(delete);
        table.close();
    }

    public static void close() {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws IOException {
        //1.判断表是否存在
        System.out.println(isTableExist("stud5"));
        //2.创建表
        createTable("stud5", "info1", "info2");
        //3.删除表
        //dropTable("stud5");
        System.out.println(isTableExist("stud5"));
        //5.创建数据测试
        //putData("stud5","1001","info1","name","xzz");
        //6.获取数据
        getData("stud5", "1001", "info1", "addr");
        //7.scan获取数据
        scanTable("stud5");
        //8.删除数据
        deleteData("stud5", "1002", "inf", "sdasd");
    }

}
