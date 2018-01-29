public class Test {


    public static void main(String[] args) {
        // 两个new出来的Integer类型的数据比较，
        //相当于把new出来的地址作比较

        Integer a0 = new Integer(1);// 普通的堆中对象
        Integer b0 = new Integer(1);
        System.out.println("new出来的 " + "a0==b0 :" + (a0 == b0));
        System.out.println(a0);

        // 调用intValue方法得到其int值
        System.out.println("调用intValue " + "a0.intValue() == b0.intValue()  " +
                (a0.intValue() == b0.intValue()));

        // 把Integer类型的变量拆箱成int类型
        int c = 1;
        System.out.println("将Integer自动拆箱 " + "a0==c: " + (a0 == c));

        // 其实也是内存地址的比较
        Integer a1 = 30; // 自动装箱，如果在-128到127之间，则值存在常量池中
        Integer b1 = 30;
        System.out.println("直接赋值的两个Integer的比较" +
                "a2 == b2: " + (a1 == b1));

        Integer a2 = 30;
        Integer b2 = 40;
        System.out.println("直接赋值的两个Integer的比较 " +
                "a6==b6: " + (a2 == b2));

        Integer a3 = 128;
        Integer b3 = 128;
        System.out.println("直接赋值的两个Integer的比较 " +
                "a5 == b5: " + (a3 == b3));

        Integer a4 = 412;
        Integer b4 = 412;
        System.out.println("直接赋值的两个Integer的比较 " +
                "a4 == b4: " + (a4 == b4));
        // 从这里看出，当给Integer直接赋值时，
        //如果在-128到127之间相等的话，它们会共用一块内存
        // 而超过这个范围，则对应的Integer对象有多少个就开辟多少个

        System.out.println("调用intValue " + "a4.intValue() == b4.intValue(): "
                + (a4.intValue() == b4.intValue()));

        Integer a7 = new Integer(412);
        Integer b7 = 412;

        System.out.println("a7 == b7:  "+ (a7 == b7));
    }

}
