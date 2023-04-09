import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FilesModule {

    public static void append(Persistent<?> persistent, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true)))) {
            bw.write(persistent.serialize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cover(List<Persistent<?>> list, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Persistent<?> persistent : list) {
                bw.write(persistent.serialize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> ArrayList<T> load(String filePath, Supplier<? extends Persistent<T>> supplier) {
        ArrayList<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Persistent<T> persistent = supplier.get();
                list.add(persistent.deserialize(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
