package com.vic;

import com.vic.pojo.Teacher;

import java.sql.*;

public class TeacherORM {
    public static void main(String[] args) {
        /**
         * 定义数据库连接的四要素
         */
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/hibernate";
        String userName="root";
        String password="";

        /**
         * 创建JDBC需要的API
         */
        Connection connection=null;
        PreparedStatement  ps=null;
        ResultSet  rs=null;

        //创建需要的实体类对象 用于动态的赋值
        Object  object=null;
        try {
            object= Class.forName("com.vic.pojo.Teacher").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            //01加载驱动
            Class.forName(driver);
            connection=DriverManager.getConnection(url,userName,password);
            //02书写sql语句
            String sql="select id,name from teacher where id=?";
            ps=connection.prepareStatement(sql);
            //03给参数赋值
            ps.setInt(1,2);
            rs=ps.executeQuery();
            //04遍历结果集
            while (rs.next()){
                ResultSetMetaData data=rs.getMetaData();
                int count=data.getColumnCount();//获得数据表中返回的列数
                for(int i=1;i<=count;i++){
                    String name=data.getColumnName(i);//获得该列的字段名
                    String method=changeName(name);//将字段名改变为setter属性名
                    String type=data.getColumnTypeName(i);//获得该字段的类型名
                    if(type.equalsIgnoreCase("int")){
                        object.getClass().getMethod(method,Integer.class).invoke(object,rs.getInt(name));
                    }else{
                        object.getClass().getMethod(method,String.class).invoke(object,rs.getString(name));
                    }
                }
                System.out.println((Teacher)object);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //05释放资源
            try{
                rs.close();
                ps.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private static String changeName(String name){
        return "set"+name.substring(0,1).toUpperCase()+name.substring(1);
    }

}
