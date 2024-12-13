package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import helpers.MD5;
import helpers.RSA;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class KeyJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel jLabelMaHoaDonHang;
	private JButton jButtonUpLoadDonHang;
	private JScrollPane scrollPane;
	private JButton jButtonMaHoaDonHang;
	private JScrollPane scrollPane_1;
	private JTextArea jTextAreaDonHang;
	private JTextArea jTextAreaMaHoaDonHang;
	private JScrollPane scrollPane_2;
	private JTextArea jTextAreaPrivateKey;
	private JScrollPane scrollPane_3;
	private JTextArea jTextAreaChuKy;
	private JTextArea jTextAreaMessages;

	private WebSocketClient webSocketClient;
	private JButton jButtonXacNhan;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KeyJFrame frame = new KeyJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public KeyJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		jLabelMaHoaDonHang = new JLabel("Mã hóa đơn hàng");
		jLabelMaHoaDonHang.setBounds(272, 0, 95, 23);
		contentPane.add(jLabelMaHoaDonHang);

		jButtonUpLoadDonHang = new JButton("Upload Đơn Hàng");
		jButtonUpLoadDonHang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upload_actionPerformed(e);
			}
		});
		jButtonUpLoadDonHang.setBounds(27, 22, 140, 39);
		contentPane.add(jButtonUpLoadDonHang);

		scrollPane = new JScrollPane();
		scrollPane.setBorder(
				new TitledBorder(null, "\u0110\u01A1n h\u00E0ng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setBounds(313, 34, 205, 89);
		contentPane.add(scrollPane);

		jTextAreaDonHang = new JTextArea();
		scrollPane.setViewportView(jTextAreaDonHang);

		jButtonMaHoaDonHang = new JButton("Mã Hóa Đơn Hàng");
		jButtonMaHoaDonHang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				encryptDonHang_actionPerformed(e);
			}
		});
		jButtonMaHoaDonHang.setBounds(27, 88, 140, 39);
		contentPane.add(jButtonMaHoaDonHang);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new TitledBorder(null, "Chu\u1ED7i m\u00E3 h\u00F3a c\u1EE7a \u0111\u01A1n h\u00E0ng",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_1.setBounds(313, 134, 205, 89);
		contentPane.add(scrollPane_1);

		jTextAreaMaHoaDonHang = new JTextArea();
		jTextAreaMaHoaDonHang.setLineWrap(true);
		scrollPane_1.setViewportView(jTextAreaMaHoaDonHang);

		scrollPane_2 = new JScrollPane();
		scrollPane_2
				.setBorder(new TitledBorder(null, "Private Key", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_2.setBounds(27, 156, 198, 96);
		contentPane.add(scrollPane_2);

		jTextAreaPrivateKey = new JTextArea();
		jTextAreaPrivateKey.setLineWrap(true);
		scrollPane_2.setViewportView(jTextAreaPrivateKey);

		JButton jButtonUploadPrivateKey = new JButton("Upload Private Key");
		jButtonUploadPrivateKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadPrivateKey_actionPerformed(e);
			}
		});
		jButtonUploadPrivateKey.setBounds(259, 234, 123, 39);
		contentPane.add(jButtonUploadPrivateKey);

		JButton jButtonKyDonHang = new JButton("Ký đơn hàng");
		jButtonKyDonHang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kyDonHang_actionPerformed(e);
			}
		});
		jButtonKyDonHang.setBounds(410, 242, 140, 48);
		contentPane.add(jButtonKyDonHang);

		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBorder(new TitledBorder(null, "Ch\u1EEF k\u00FD \u0111i\u1EC7n t\u1EED", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		scrollPane_3.setBounds(27, 281, 205, 86);
		contentPane.add(scrollPane_3);

		jTextAreaChuKy = new JTextArea();
		jTextAreaChuKy.setLineWrap(true);
		scrollPane_3.setViewportView(jTextAreaChuKy);

		JScrollPane messagePane = new JScrollPane();
		messagePane.setBorder(new TitledBorder(null, "Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		messagePane.setBounds(313, 320, 400, 200);
		contentPane.add(messagePane);

		jTextAreaMessages = new JTextArea();
		jTextAreaMessages.setLineWrap(true);
		messagePane.setViewportView(jTextAreaMessages);

		jButtonXacNhan = new JButton("Xác nhận");
		jButtonXacNhan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xacnhan_actionPerformed(e);
			}
		});
		jButtonXacNhan.setBounds(56, 398, 128, 48);
		contentPane.add(jButtonXacNhan);

		connectToWebSocket();
	}

	private void connectToWebSocket() {
		try {
			webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/projectGroup2/key")) {
				@Override
				public void onOpen(ServerHandshake handshakedata) {
					jTextAreaMessages.append("Connected to WebSocket server.\n");
				}

				@Override
				public void onMessage(String message) {
					jTextAreaMessages.append("Received: " + message + "\n");
					jTextAreaMaHoaDonHang.setText(message);
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					jTextAreaMessages.append("Disconnected from server.\n");
				}

				@Override
				public void onError(Exception ex) {
					jTextAreaMessages.append("Error: " + ex.getMessage() + "\n");
				}

			};

			webSocketClient.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void xacnhan_actionPerformed(ActionEvent e) {
		try {
			webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/projectGroup2/key")) {
				@Override
				public void onOpen(ServerHandshake handshakedata) {
					jTextAreaMessages.append("Connected to WebSocket server.\n");
					// Gửi tin nhắn khi kết nối thành công
					sendMessageToClients(jTextAreaChuKy.getText().trim());
				}

				@Override
				public void onMessage(String message) {
					jTextAreaMessages.append("Received: " + message + "\n");
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					jTextAreaMessages.append("Disconnected from server.\n");
				}

				@Override
				public void onError(Exception ex) {
					jTextAreaMessages.append("Error: " + ex.getMessage() + "\n");
				}
			};

			webSocketClient.connect();

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	// Thêm hàm để gửi tin nhắn
	private void sendMessageToClients(String message) {
		if (webSocketClient != null && webSocketClient.isOpen()) {
			webSocketClient.send(message);
			jTextAreaMessages.append("Sent: " + message + "\n");
		} else {
			jTextAreaMessages.append("WebSocket is not connected.\n");
		}
	}

	public void upload_actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();

			// Kiểm tra xem file có phải là .txt không
			if (selectedFile.getName().endsWith(".txt")) {
				try {
					// Đọc nội dung file
					BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
					String line;
					StringBuilder fileContent = new StringBuilder();

					// Đọc từng dòng và thêm vào StringBuilder
					while ((line = reader.readLine()) != null) {
						fileContent.append(line).append("\n"); // Thêm dòng vào StringBuilder
					}

					// In ra nội dung của file
					System.out.println(fileContent.toString());
					jTextAreaDonHang.setText(fileContent.toString());
					reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Chỉ chấp nhận file .txt!");
			}
		}
	}

	public void encryptDonHang_actionPerformed(ActionEvent e) {
		String donhang = jTextAreaDonHang.getText();
		MD5 md5 = new MD5();
		jTextAreaMaHoaDonHang.setText(md5.hashMD5(donhang));
	}

	public void uploadPrivateKey_actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();

			// Kiểm tra xem file có phải là .txt không
			if (selectedFile.getName().endsWith(".txt")) {
				try {
					// Đọc nội dung file
					BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
					String line;
					StringBuilder fileContent = new StringBuilder();

					// Đọc từng dòng và thêm vào StringBuilder
					while ((line = reader.readLine()) != null) {
						fileContent.append(line).append("\n"); // Thêm dòng vào StringBuilder
					}

					// In ra nội dung của file
					System.out.println(fileContent.toString());
					jTextAreaPrivateKey.setText(fileContent.toString());

					reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Chỉ chấp nhận file .txt!");
			}
		}
	}

	public void kyDonHang_actionPerformed(ActionEvent e) {
		RSA rsa = new RSA();
		try {
			System.out.println(rsa.encryptWithPrivateKey(jTextAreaMaHoaDonHang.getText().trim(),
					jTextAreaPrivateKey.getText().trim()));
			jTextAreaChuKy.setText(rsa.encryptWithPrivateKey(jTextAreaMaHoaDonHang.getText().trim(),
					jTextAreaPrivateKey.getText().trim()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
