package server;

class Palette {
    private static Color[] colors = {Color.WHITE, Color.BLUE,   Color.CYAN,
                                     Color.GREEN, Color.PURPLE, Color.RED,
                                     Color.YELLOW};
    private static int idx = 0;

    static synchronized Color getColor() {
        Color ret = colors[idx];
        idx = (idx + 1) % colors.length;
        return ret;
    }
}