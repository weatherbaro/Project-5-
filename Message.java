public class Message implements Persistent<Message> {

    private String from;
    private String to;
    private String content;
    private long time;

    public Message(String from, String to, String content, long time) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static Message createBy(User from, User to, String content) {
        return new Message(from.getEmail(), to.getEmail(), content, System.currentTimeMillis());
    }

    @Override
    public String serialize() {
        return String.format("%s,%s,%s,%d", from, to, content, time);
    }

    @Override
    public Message deserialize(String s) {
        String[] split = s.split(",");
        return new Message(split[0], split[1], split[2], Long.parseLong(split[3]));
    }
}
