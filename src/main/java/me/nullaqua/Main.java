package me.nullaqua;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import me.lanzhi.api.awt.BluestarLayout;
import me.lanzhi.api.awt.BluestarLayoutData;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class Main extends JFrame
{
    Main()
    {
        super("FileToImage - by NullAqua");
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(500, 200);
        setVisible(true);
        LafManager.install(new DarculaTheme());
        setVisible(true);
        JTabbedPane tabbedPane = new JTabbedPane();
        this.setContentPane(tabbedPane);

        tabbedPane.addTab("FileToImage", fileToImage());
        tabbedPane.addTab("ImageToFile", imageToFile());

        setVisible(true);

        addWindowFocusListener(new java.awt.event.WindowAdapter()
        {
            public void windowGainedFocus(java.awt.event.WindowEvent e)
            {
                Main.this.setSize(500, 200);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowOpened(java.awt.event.WindowEvent e)
            {
                Main.this.setSize(500, 200);
            }
        });
    }

    public static void main(String[] args)
    {
        new Main();
    }

    private JPanel fileToImage()
    {
        var panel = new JPanel();
        panel.setLayout(new BluestarLayout());
        BluestarLayoutData data;
        data = new BluestarLayoutData(1, 4);
        data.setInsets(new Insets(0, 5, 0, 0));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FRONT);
        panel.add(new JLabel("File:"), data);
        data = new BluestarLayoutData(1, 4);
        data.setInsets(new Insets(0, 55, 0, 100));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FILL);
        JTextField fileField = new JTextField();
        panel.add(fileField, data);
        data = new BluestarLayoutData(1, 4);
        data.setInsets(new Insets(0, 0, 0, 10));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.BACK);
        JButton fileButton = new JButton("Browse");
        panel.add(fileButton, data);
        fileButton.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileHidingEnabled(true);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Select a file");
            chooser.showOpenDialog(this);
            File file = chooser.getSelectedFile();
            if (file != null)
            {
                fileField.setText(file.getAbsolutePath());
            }
        });
        data = new BluestarLayoutData(1, 4, 0, 1);
        data.setInsets(new Insets(0, 5, 0, 0));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FRONT);
        panel.add(new JLabel("Image:"), data);
        data = new BluestarLayoutData(1, 4, 0, 1);
        data.setInsets(new Insets(0, 55, 0, 100));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FILL);
        JTextField imageField = new JTextField();
        panel.add(imageField, data);
        data = new BluestarLayoutData(1, 4, 0, 1);
        data.setInsets(new Insets(0, 0, 0, 10));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.BACK);
        JButton imageButton = new JButton("Browse");
        panel.add(imageButton, data);
        imageButton.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileHidingEnabled(true);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Select output image");
            chooser.showOpenDialog(this);
            File file = chooser.getSelectedFile();
            if (file != null)
            {
                imageField.setText(file.getAbsolutePath());
            }
        });
        //第三行2个输入框,分别是宽度和高度
        data = new BluestarLayoutData(2, 4, 0, 2);
        data.setInsets(new Insets(0, 5, 0, 0));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FRONT);
        panel.add(new JLabel("Width:"), data);
        data = new BluestarLayoutData(2, 4, 0, 2);
        data.setInsets(new Insets(0, 55, 0, 100));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FILL);
        JTextField widthField = new JTextField();
        panel.add(widthField, data);
        data = new BluestarLayoutData(2, 4, 1, 2);
        data.setInsets(new Insets(0, 5, 0, 0));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FRONT);
        panel.add(new JLabel("Height:"), data);
        data = new BluestarLayoutData(2, 4, 1, 2);
        data.setInsets(new Insets(0, 55, 0, 100));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FILL);
        JTextField heightField = new JTextField();
        panel.add(heightField, data);
        //第四行一个按钮
        JButton run = new JButton("Run");
        data = new BluestarLayoutData(1, 4, 0, 3);
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.CENTER);
        panel.add(run, data);
        Runnable r = () ->
        {
            File file = new File(fileField.getText());
            File image = new File(imageField.getText());
            int width = -1, height = -1;
            try
            {
                width = Integer.parseInt(widthField.getText());
                height = Integer.parseInt(heightField.getText());
            }
            catch (Throwable ignored)
            {
            }
            try
            {
                FileToImage.write(file, image, height, width);
                JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Throwable ex)
            {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            run.setEnabled(true);
            run.setText("Run");
        };
        ActionListener listener = e ->
        {
            run.setText("Running...");
            run.setEnabled(false);
            new Thread(r).start();
        };
        run.addActionListener(listener);
        fileInput(fileField);
        fileInput(imageField);
        return panel;
    }

    private JPanel imageToFile()
    {
        var panel = new JPanel();
        panel.setLayout(new BluestarLayout());
        BluestarLayoutData data;
        data = new BluestarLayoutData(1, 4);
        data.setInsets(new Insets(0, 5, 0, 0));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FRONT);
        panel.add(new JLabel("Image:"), data);
        data = new BluestarLayoutData(1, 4);
        data.setInsets(new Insets(0, 55, 0, 100));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FILL);
        JTextField imageField = new JTextField();
        panel.add(imageField, data);
        data = new BluestarLayoutData(1, 4);
        data.setInsets(new Insets(0, 0, 0, 10));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.BACK);
        JButton fileButton = new JButton("Browse");
        panel.add(fileButton, data);
        fileButton.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileHidingEnabled(true);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Select a image");
            chooser.showOpenDialog(this);
            File file = chooser.getSelectedFile();
            if (file != null)
            {
                imageField.setText(file.getAbsolutePath());
            }
        });
        data = new BluestarLayoutData(1, 4, 0, 1);
        data.setInsets(new Insets(0, 5, 0, 0));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FRONT);
        panel.add(new JLabel("Output:"), data);
        data = new BluestarLayoutData(1, 4, 0, 1);
        data.setInsets(new Insets(0, 55, 0, 100));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.FILL);
        JTextField ofField = new JTextField();
        panel.add(ofField, data);
        data = new BluestarLayoutData(1, 4, 0, 1);
        data.setInsets(new Insets(0, 0, 0, 10));
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.BACK);
        JButton imageButton = new JButton("Browse");
        panel.add(imageButton, data);
        imageButton.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileHidingEnabled(true);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Select output image");
            chooser.showOpenDialog(this);
            File file = chooser.getSelectedFile();
            if (file != null)
            {
                ofField.setText(file.getAbsolutePath());
            }
        });
        JButton run = new JButton("Run");
        data = new BluestarLayoutData(1, 4, 0, 3);
        data.setPortraitAlignment(BluestarLayoutData.CENTER);
        data.setTransverseAlignment(BluestarLayoutData.CENTER);
        panel.add(run, data);
        Runnable r = () ->
        {
            File file = new File(ofField.getText());
            File image = new File(imageField.getText());
            try
            {
                FileToImage.read(image, file);
                JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Throwable ex)
            {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            run.setEnabled(true);
            run.setText("Run");
        };
        ActionListener listener = e ->
        {
            run.setText("Running...");
            run.setEnabled(false);
            new Thread(r).start();
        };
        run.addActionListener(listener);
        fileInput(imageField);
        fileInput(ofField);
        return panel;
    }

    private void fileInput(JTextField component)
    {
        component.setTransferHandler(new TransferHandler()
        {
            @Override
            public boolean importData(TransferSupport support)
            {
                if (!canImport(support))
                {
                    return false;
                }
                Transferable t = support.getTransferable();
                try
                {
                    List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (files.size() == 1)
                    {
                        component.setText(files.get(0).getAbsolutePath());
                    }
                }
                catch (Throwable ignored)
                {
                }
                return true;
            }

            @Override
            public boolean canImport(TransferSupport support)
            {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }
        });
    }
}