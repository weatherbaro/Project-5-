package cn.rebellion.Date20230408.reference.xing.me;

public interface Persistent<T> {

    String serialize();

    T deserialize(String s);

}
