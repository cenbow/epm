package br.net.woodstock.epm.signer.common;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import br.net.woodstock.rockframework.utils.ArrayUtils;

public final class FilePanel extends JPanel {

	private static final long	serialVersionUID	= -2336931609465577799L;

	private static FilePanel	instance			= new FilePanel();

	private FileSystemView		fileSystemView;

	private JScrollPane			spFiles;

	private JTree				tFiles;

	private JList				lSelectedFiles;

	private JScrollPane			spSelectedFiles;

	private JButton				btBack;

	private JButton				btNext;

	private FilePanel() {
		super();
		this.init();
	}

	protected void init() {
		this.fileSystemView = FileSystemView.getFileSystemView();

		this.buildGUI();
		this.addGUIEvents();
	}

	protected void buildGUI() {
		// Layout
		this.setLayout(new GridBagLayout());

		// Line
		int line = 0;

		// Tree
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(ApplicationHolder.getInstance().getMessage(Constants.LABEL_MY_COMPUTER));
		for (File file : this.fileSystemView.getRoots()) {
			if (this.fileSystemView.isTraversable(file).booleanValue()) {
				FileObject fileObject = new FileObject(this.fileSystemView.getSystemDisplayName(file), file);
				FileMutableTreeNode node = new FileMutableTreeNode(fileObject);
				root.add(node);
			}
		}

		this.tFiles = new JTree(root);
		this.tFiles.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.spFiles = new JScrollPane(this.tFiles);
		this.spFiles.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.spFiles.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.spFiles.setPreferredSize(new Dimension(300, 200));

		this.add(this.spFiles, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL | GridBagConstraints.VERTICAL));

		// Combo
		DefaultListModel model = new DefaultListModel();
		this.lSelectedFiles = new JList(model);
		this.spSelectedFiles = new JScrollPane(this.lSelectedFiles);
		this.spSelectedFiles.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.spSelectedFiles.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.spSelectedFiles.setPreferredSize(new Dimension(200, 200));
		this.add(this.spSelectedFiles, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL | GridBagConstraints.VERTICAL));
		line++;

		// Buttons
		this.btBack = new JButton(ApplicationHolder.getInstance().getMessage(Constants.LABEL_BACK));
		// this.btBack.setEnabled(false);
		this.add(this.btBack, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));

		this.btNext = new JButton(ApplicationHolder.getInstance().getMessage(Constants.LABEL_NEXT));
		// this.btNext.setEnabled(false);
		this.add(this.btNext, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));

	}

	protected void addGUIEvents() {

		this.tFiles.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				int selectedRow = FilePanel.this.getTFiles().getRowForLocation(e.getX(), e.getY());
				if (selectedRow != -1) {
					if (e.getClickCount() == 2) {
						TreePath path = FilePanel.this.getTFiles().getPathForLocation(e.getX(), e.getY());
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						if (node instanceof FileMutableTreeNode) {
							FileMutableTreeNode fmtn = (FileMutableTreeNode) node;
							if (fmtn.getFile().isFile()) {
								DefaultListModel model = (DefaultListModel) FilePanel.this.getlSelectedFiles().getModel();
								model.addElement(fmtn.getUserObject());
							}
						}
					}
				}
			}

		});

		this.tFiles.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					TreePath path = FilePanel.this.getTFiles().getSelectionPath();
					Object[] nodes = path.getPath();
					for (Object tmp : nodes) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) tmp;
						if (node instanceof FileMutableTreeNode) {
							FileMutableTreeNode fmtn = (FileMutableTreeNode) node;
							if (fmtn.getFile().isFile()) {
								DefaultListModel model = (DefaultListModel) FilePanel.this.getlSelectedFiles().getModel();
								model.addElement(fmtn.getUserObject());
							}
						}
					}
				}
			}

		});

		this.tFiles.addTreeWillExpandListener(new TreeWillExpandListener() {

			@Override
			public void treeWillCollapse(final TreeExpansionEvent event) throws ExpandVetoException {
				FilePanel.this.getSpFiles().revalidate();
			}

			@Override
			public void treeWillExpand(final TreeExpansionEvent event) throws ExpandVetoException {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
				if (node != null) {
					if (node instanceof FileMutableTreeNode) {
						FileMutableTreeNode fmtn = (FileMutableTreeNode) node;
						if (fmtn.getFile().canRead()) {
							List<File> files = ArrayUtils.toList(FilePanel.this.getFileSystemView().getFiles(fmtn.getFile(), false));
							Collections.sort(files, FileComparator.getInstance());
							for (File file : files) {
								if (file.canRead()) {
									FileObject fileObject = new FileObject(FilePanel.this.getFileSystemView().getSystemDisplayName(file), file);
									FileMutableTreeNode child = new FileMutableTreeNode(fileObject);
									node.add(child);
								}
							}
						}
					}
				}
				FilePanel.this.getSpFiles().revalidate();
			}

		});

		this.lSelectedFiles.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					JList list = (JList) e.getSource();
					DefaultListModel model = (DefaultListModel) list.getModel();
					int index = list.getSelectedIndex();
					if (index >= 0) {
						model.remove(index);
					}
				}
			}

		});

		this.lSelectedFiles.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					JList list = (JList) e.getSource();
					DefaultListModel model = (DefaultListModel) list.getModel();
					int index = list.getSelectedIndex();
					if (index >= 0) {
						model.remove(index);
					}
				}
			}

		});

		this.btBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(1, false);
				ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(0, true);
				ApplicationPanel.getInstance().getTabbedPane().setSelectedIndex(0);
			}

		});

		this.btNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				JList list = FilePanel.this.getlSelectedFiles();
				DefaultListModel model = (DefaultListModel) list.getModel();
				if (model.getSize() > 0) {
					Set<File> files = new HashSet<File>();
					int size = model.getSize();
					for (int i = 0; i < size; i++) {
						FileObject fileObject = (FileObject) model.get(i);
						files.add(fileObject.getFile());
					}
					ApplicationHolder.getInstance().setSelectedFiles(files);
					ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(1, false);
					ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(2, true);
					ApplicationPanel.getInstance().getTabbedPane().setSelectedIndex(2);
				} else {
					JOptionPane.showMessageDialog(FilePanel.this, ApplicationHolder.getInstance().getMessage(Constants.MSG_ERROR_NO_FILE_SELECTED), ApplicationHolder.getInstance().getMessage(Constants.LABEL_ERROR), JOptionPane.ERROR_MESSAGE);
				}
			}

		});
	}

	public FileSystemView getFileSystemView() {
		return this.fileSystemView;
	}

	public JScrollPane getSpFiles() {
		return this.spFiles;
	}

	public JTree getTFiles() {
		return this.tFiles;
	}

	public JList getlSelectedFiles() {
		return this.lSelectedFiles;
	}

	public JScrollPane getSpSelectedFiles() {
		return this.spSelectedFiles;
	}

	// Instance
	public static FilePanel getInstance() {
		return FilePanel.instance;
	}

}
