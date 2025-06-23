import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnhancedBarGraph extends JPanel {
    private List<Double> dataPoints;
    private List<Color> barColors;
    private List<String> barLabels;
    private int barThickness = 40;

    public EnhancedBarGraph() {
        dataPoints = new ArrayList<>();
        barColors = new ArrayList<>();
        barLabels = new ArrayList<>();
    }


    private String title;

    public void setTitle(String title) {
        this.title = title;
    }
    public void addBarData(Color color, double value, String label) {
        barColors.add(color);
        dataPoints.add(value);
        barLabels.add(label);
    }

    public void setBarThickness(int thickness) {
        this.barThickness = thickness;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dataPoints.isEmpty() || barColors.isEmpty()) return;

        if (title != null) {
            Font titleFont = new Font("Arial", Font.BOLD, 16);
            FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
            int titleWidth = titleFontMetrics.stringWidth(title);
            int titleX = (getWidth() - titleWidth) / 2;
            int titleY = 20;
            g.setFont(titleFont);
            g.setColor(Color.BLACK);
            g.drawString(title, titleX, titleY);
        }
        int chartWidth = getWidth() - 100;
        int chartHeight = getHeight() - 100;
        double maxDataPoint = dataPoints.stream().mapToDouble(v -> v).max().orElse(1);

        int numBars = dataPoints.size();
        int totalBarPadding = (numBars - 1) * 2;
        int totalBarThickness = (numBars * barThickness);
        int barSpacing = (chartWidth - totalBarThickness - totalBarPadding) / numBars;

        int x = 50 + barSpacing;
        for (int i = 0; i < dataPoints.size(); i++) {
            int barHeight = (int) ((dataPoints.get(i) / maxDataPoint) * chartHeight);
            int y = getHeight() - barHeight - 40;
            drawBar(g, x, y, barThickness, barHeight, barColors.get(i));
            drawBarLabel(g, x, getHeight() - 5, barLabels.get(i));
            x += (barThickness + barSpacing);
        }

        drawAxes(g, chartWidth, chartHeight, maxDataPoint);
    }

    /**
     * Draws a single bar on the graph.
     *
     * @param g        The Graphics object.
     * @param x        The x-coordinate of the bar.
     * @param y        The y-coordinate of the bar.
     * @param width    The width of the bar.
     * @param height   The height of the bar.
     * @param color    The color of the bar.
     */
    private void drawBar(Graphics g, int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    /**
     * Draws the label below a bar.
     *
     * @param g        The Graphics object.
     * @param x        The x-coordinate of the label.
     * @param y        The y-coordinate of the label.
     * @param label    The label text.
     */
    private void drawBarLabel(Graphics g, int x, int y, String label) {
        g.drawString(label, x, y);
    }

    /**
     * Draws the axes and labels/**

Draws the axes and labels of the graph.

@param g               The Graphics object.
@param chartWidth      The width of the chart area.
@param chartHeight     The height of the chart area.
@param maxDataPoint    The maximum data point value.
*/
private void drawAxes(Graphics g, int chartWidth, int chartHeight, double maxDataPoint) {
    Graphics2D g2d = (Graphics2D) g;
    FontMetrics fm = g2d.getFontMetrics();
    int labelHeight = fm.getHeight();
    int maxNumLabels = chartHeight / (labelHeight * 2);
    int numYTicks = Math.min(maxNumLabels, 10);
    double yIncrement = (double) chartHeight / numYTicks;
    g.drawLine(50, getHeight() - 40, 50, 20);
    g.drawLine(50, getHeight() - 40, chartWidth + 50, getHeight() - 40);
    for (int i = 0; i <= numYTicks; i++) {
    int y = getHeight() - 40 - (int) (i * yIncrement);
    g.drawLine(45, y, 55, y);
    String yLabel = String.format("%.1f", (maxDataPoint / numYTicks) * i);
    int labelWidth = fm.stringWidth(yLabel);
    g.drawString(yLabel, 50 - labelWidth - 10, y + (labelHeight / 4));
    }
    }
    
    }