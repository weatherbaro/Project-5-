public interface Persistent<T> {

    String serialize();

    T deserialize(String s);

}
