import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Download {
    public static void download(List<Bin> bins, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(toJson(bins, 0));
            System.out.println("Fichier téléchargé : " + fileName);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }

    public static String toJson(List<Bin> bins, int indentLevel) {
        StringBuilder json = new StringBuilder();
        String indent = " ".repeat(indentLevel * 2);
        json.append("[\n");
        for (int i = 0; i < bins.size(); i++) {
            json.append(indent).append(binToJson(bins.get(i), indentLevel + 1));
            if (i < bins.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append(indent).append("]");
        return json.toString();
    }

    private static String binToJson(Bin bin, int indentLevel) {
        StringBuilder json = new StringBuilder();
        String indent = " ".repeat(indentLevel * 2);
        json.append(indent).append("{\n");
        json.append(indent).append("  \"width\": ").append(bin.width).append(",\n");
        json.append(indent).append("  \"height\": ").append(bin.height).append(",\n");
        if (bin.item != null) {
            json.append(indent).append("  \"item\": {\n");
            json.append(indent).append("    \"id\": ").append(bin.item.id).append(",\n");
            json.append(indent).append("    \"width\": ").append(bin.item.width).append(",\n");
            json.append(indent).append("    \"height\": ").append(bin.item.height).append("\n");
            json.append(indent).append("  },\n");
        } else {
            json.append(indent).append("  \"item\": null,\n");
        }
        json.append(indent).append("  \"sousBins\": [\n");
        for (int i = 0; i < bin.sousBins.size(); i++) {
            json.append(binToJson(bin.sousBins.get(i), indentLevel + 2));
            if (i < bin.sousBins.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append(indent).append("  ]\n");
        json.append(indent).append("}");
        return json.toString();
    }
}