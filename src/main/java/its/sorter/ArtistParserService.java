/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package its.sorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author mihailo-jankovic
 */
public class ArtistParserService {

    public ImportResult parse(String text) {
        if (text == null || text.isBlank()) {
            return new ImportResult(List.of(), List.of());
        }

        List<Row> sorted = new ArrayList<>();
        List<Row> candidates = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (String line : text.split("\\r?\\n")) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s*[-–—]\\s*", 2);
            String artist = parts[0].trim();
            String song = parts.length > 1 ? parts[1].trim() : "";

            String key = (artist + "|" + song).toLowerCase(Locale.ROOT);

// ako je bez pesme, a već postoji isti artist sa pesmom → preskoči
            if (song.isBlank()) {
                boolean artistAlreadyHasSong
                        = seen.stream().anyMatch(k -> k.startsWith(artist.toLowerCase(Locale.ROOT) + "|")
                        && !k.endsWith("|"));

                if (artistAlreadyHasSong) {
                    continue;
                }
            }

// standardna deduplikacija
            if (!seen.add(key)) {
                continue;
            }

            if (artist.split("\\s+").length == 1) {
                sorted.add(new Row(firstLetter(artist), artist, song, "BEND"));
            } else {
                candidates.add(new Row("?", artist, song, ""));
            }
        }

        sorted.sort(Comparator.comparing(Row::getLetter).thenComparing(Row::getArtist, String.CASE_INSENSITIVE_ORDER));

        return new ImportResult(sorted, candidates);
    }

    private String firstLetter(String s) {
        return s.substring(0, 1).toUpperCase(Locale.ROOT);
    }
}
