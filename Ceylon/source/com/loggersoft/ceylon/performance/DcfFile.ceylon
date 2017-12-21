import ceylon.file { File }

class DcfFile(File file) {

    shared void processFile(Boolean(DcfSegment) segmentRetreived, Anything(String) invalidLine) {
        try (reader = file.Reader()) {
            while (exists line = reader.readLine()) {
                value token = line.trimmed;
                if (!token.startsWith("SEG:")) {
                    continue;
                }
                value parts = token.split(':'.equals);
                if (parts.size < 6) {
                    invalidLine(line);
                    continue;
                }
                variable String? clustId = null;
                variable Float lat1 = 0.0;
                variable Float lon1 = 0.0;
                variable Float lat2 = 0.0;
                variable Float lon2 = 0.0;
                variable Boolean isInvalid = false;
                variable Boolean isNextClustId = false;
                for (i->p in parts.indexed) {
                    if (1 <= i <= 4) {
                        if (is Float result = Float.parse(p)) {
                            if (i == 1) {
                                lon1 = result;
                            } else if (i == 2) {
                                lat1 = result;
                            } else if (i == 3) {
                                lon2 = result;
                            } else if (i == 4) {
                                lat2 = result;
                            }

                        } else {
                            isInvalid = true;
                            break;
                        }

                    } else if (isNextClustId) {
                        clustId = p;
                        break;

                    } else if (p.startsWith("ClustID")) {
                        isNextClustId = true;
                    }
                }
                if (isInvalid) {
                    invalidLine(line);
                    continue;
                }
                value cluster = clustId;
                if (!exists cluster) {
                    invalidLine(line);
                    continue;
                }
                if (!segmentRetreived(DcfSegment(Position(lat1, lon1), Position(lat2, lon2), cluster))) {
                    break;
                }
            }
        }
    }

}