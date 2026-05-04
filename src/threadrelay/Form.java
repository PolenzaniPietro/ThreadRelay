package threadrelay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Form extends JFrame {

    private static final Pattern PROGRESS_PATTERN = Pattern.compile("Runner\\s+(\\d+)\\s+->\\s+(\\d+)");
    private static final int FONT_SWITCH_PERCENT = 67;
    private static final Font PROGRESS_FONT_NORMAL = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font PROGRESS_FONT_HIGHLIGHT = new Font("Segoe UI", Font.BOLD | Font.ITALIC, 18);
    private static final Font VALUE_FONT_NORMAL = new Font("Consolas", Font.BOLD, 14);
    private static final Font VALUE_FONT_HIGHLIGHT = new Font("Consolas", Font.BOLD, 18);
    private static final Color[] RUNNER_COLORS = new Color[] {
        new Color(0, 153, 255),
        new Color(0, 200, 120),
        new Color(255, 166, 0),
        new Color(220, 68, 255)
    };
    private static final Color[] RUNNER_HIGHLIGHT_COLORS = new Color[] {
        new Color(0, 93, 184),
        new Color(11, 122, 79),
        new Color(194, 100, 0),
        new Color(156, 33, 194)
    };

    private final JProgressBar[] progressBars = new JProgressBar[4];
    private final JLabel[] progressLabels = new JLabel[4];
    private final JButton startButton = new JButton("Avvia");
    private final JButton pauseButton = new JButton("Pausa");
    private final JButton resumeButton = new JButton("Riprendi");
    private final JLabel statusLabel = new JLabel("Pronto");
    private Staffetta currentStaffetta;

    public Form() {
        setModernLookAndFeel();
        initUi();
        bindActions();
    }

    private void setModernLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void initUi() {
        setTitle("Thread Relay - Staffetta");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        rootPanel.setBackground(new Color(241, 245, 249));

        JLabel title = new JLabel("Simulazione Staffetta");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("4 runner con passaggio testimone a quota 90");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(71, 85, 105));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setOpaque(false);
        header.add(title);
        header.add(subtitle);

        JPanel barsPanel = new JPanel(new GridLayout(4, 1, 10, 14));
        barsPanel.setOpaque(true);
        barsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            new EmptyBorder(18, 18, 18, 18)
        ));
        barsPanel.setBackground(Color.WHITE);
        for (int i = 0; i < progressBars.length; i++) {
            JPanel row = new JPanel(new BorderLayout(14, 0));
            row.setOpaque(false);

            JLabel runnerLabel = new JLabel("Runner " + (i + 1));
            runnerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            runnerLabel.setForeground(new Color(15, 23, 42));
            runnerLabel.setPreferredSize(new Dimension(110, 30));
            row.add(runnerLabel, BorderLayout.WEST);

            JProgressBar progressBar = new JProgressBar(0, 99);
            progressBar.setStringPainted(true);
            progressBar.setValue(0);
            progressBar.setForeground(RUNNER_COLORS[i]);
            progressBar.setBackground(new Color(226, 232, 240));
            progressBar.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
            progressBar.setFont(PROGRESS_FONT_NORMAL);
            progressBar.setString("0%");
            progressBars[i] = progressBar;
            row.add(progressBar, BorderLayout.CENTER);

            JLabel valueLabel = new JLabel("0 / 99");
            valueLabel.setFont(VALUE_FONT_NORMAL);
            valueLabel.setForeground(new Color(51, 65, 85));
            valueLabel.setPreferredSize(new Dimension(76, 30));
            progressLabels[i] = valueLabel;
            row.add(valueLabel, BorderLayout.EAST);

            barsPanel.add(row);
        }

        JPanel controls = new JPanel(new BorderLayout(10, 0));
        controls.setOpaque(false);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusLabel.setForeground(new Color(14, 116, 144));
        controls.add(statusLabel, BorderLayout.WEST);

        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(37, 99, 235));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        startButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        pauseButton.setFocusPainted(false);
        pauseButton.setBackground(new Color(245, 158, 11));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pauseButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        resumeButton.setFocusPainted(false);
        resumeButton.setBackground(new Color(22, 163, 74));
        resumeButton.setForeground(Color.WHITE);
        resumeButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        resumeButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(startButton);
        actions.add(pauseButton);
        actions.add(resumeButton);
        controls.add(actions, BorderLayout.EAST);

        rootPanel.add(header, BorderLayout.NORTH);
        rootPanel.add(barsPanel, BorderLayout.CENTER);
        rootPanel.add(controls, BorderLayout.SOUTH);

        add(rootPanel, BorderLayout.CENTER);

        setMinimumSize(new Dimension(920, 620));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void bindActions() {
        startButton.addActionListener(e -> startRace());
        pauseButton.addActionListener(e -> pauseRace());
        resumeButton.addActionListener(e -> resumeRace());
    }

    private void startRace() {
        startButton.setEnabled(false);
        statusLabel.setText("Staffetta in corso...");
        for (JProgressBar progressBar : progressBars) {
            progressBar.setValue(0);
            progressBar.setString("0%");
        }
        for (int i = 0; i < progressLabels.length; i++) {
            progressLabels[i].setText("0 / 99");
            progressLabels[i].setFont(VALUE_FONT_NORMAL);
            progressBars[i].setFont(PROGRESS_FONT_NORMAL);
            progressBars[i].setForeground(RUNNER_COLORS[i]);
        }

        currentStaffetta = new Staffetta();
        Thread raceThread = new Thread(() -> {
            PrintStream originalOut = System.out;
            PrintStream parsingOut = new PrintStream(new LineParserOutputStream(originalOut), true);
            try {
                System.setOut(parsingOut);
                currentStaffetta.startRace();
            } finally {
                parsingOut.flush();
                System.setOut(originalOut);
                SwingUtilities.invokeLater(() -> {
                    startButton.setEnabled(true);
                    statusLabel.setText("Completata");
                    currentStaffetta = null;
                });
            }
        }, "race-thread");
        raceThread.start();
    }

    private void pauseRace() {
        if (currentStaffetta == null) {
            return;
        }
        currentStaffetta.pauseRace();
        statusLabel.setText("In pausa");
    }

    private void resumeRace() {
        if (currentStaffetta == null) {
            return;
        }
        currentStaffetta.resumeRace();
        statusLabel.setText("Staffetta in corso...");
    }

    private void updateProgressFromLine(String line) {
        Matcher matcher = PROGRESS_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return;
        }

        int runnerIndex = Integer.parseInt(matcher.group(1)) - 1;
        int progress = Integer.parseInt(matcher.group(2));
        if (runnerIndex < 0 || runnerIndex >= progressBars.length) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            progressBars[runnerIndex].setValue(progress);
            int percent = Math.max(0, Math.min(100, (int) Math.round((progress / 99.0) * 100)));
            progressBars[runnerIndex].setString(percent + "%");
            progressLabels[runnerIndex].setText(progress + " / 99");
            boolean highlight = percent >= FONT_SWITCH_PERCENT;
            progressBars[runnerIndex].setFont(highlight ? PROGRESS_FONT_HIGHLIGHT : PROGRESS_FONT_NORMAL);
            progressLabels[runnerIndex].setFont(highlight ? VALUE_FONT_HIGHLIGHT : VALUE_FONT_NORMAL);
            progressBars[runnerIndex].setForeground(highlight ? RUNNER_HIGHLIGHT_COLORS[runnerIndex] : RUNNER_COLORS[runnerIndex]);
        });
    }

    private class LineParserOutputStream extends OutputStream {

        private final PrintStream delegate;
        private final StringBuilder currentLine = new StringBuilder();

        LineParserOutputStream(PrintStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public void write(int b) throws IOException {
            delegate.write(b);
            char ch = (char) b;
            if (ch == '\n') {
                String line = currentLine.toString().trim();
                currentLine.setLength(0);
                updateProgressFromLine(line);
            } else if (ch != '\r') {
                currentLine.append(ch);
            }
        }

        @Override
        public void flush() throws IOException {
            delegate.flush();
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new Form().setVisible(true));
    }
}
