package com.sms.gateway.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author chenglutao on 2019/10/25
 */
public class Cfg {
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	private static final String XML_HEAD = String
			.valueOf(String.valueOf((new StringBuffer("<?xml version=\"1.0\" encoding=\""))
					.append(System.getProperty("file.encoding")).append("\"?>")));
	private static String indent = "  ";
	private boolean isDirty;
	private Document doc;
	private Element root;
	private String file;

	public Cfg(String url) throws IOException {
		this(url, false);
	}

	public Cfg(String url, boolean create) throws IOException {
		if (url == null) {
			throw new IllegalArgumentException("url is null");
		} else {
			if (url.indexOf(58) > 1) {
				this.file = url;
			} else {
				this.file = (new File(url)).toURL().toString();
			}

			new URL(this.file);

			try {
				this.load();
			} catch (FileNotFoundException var4) {
				if (!create) {
					throw var4;
				} else {
					this.loadXMLParser();
					this.doc = builder.newDocument();
					this.root = this.doc.createElement("config");
					this.doc.appendChild(this.root);
					this.isDirty = true;
					this.flush();
				}
			}
		}
	}

	public Args getArgs(String key) {
		Map args = new HashMap();
		String[] children = this.childrenNames(key);

		for (int i = 0; i < children.length; ++i) {
			args.put(children[i],
					this.get(String.valueOf(String.valueOf(
							(new StringBuffer(String.valueOf(String.valueOf(key)))).append('/').append(children[i]))),
							(String) null));
		}

		return new Args(args);
	}

	private static void writeIndent(PrintWriter pw, int level) {
		for (int i = 0; i < level; ++i) {
			pw.print(indent);
		}

	}

	private static void writeNode(Node node, PrintWriter pw, int deep) {
		int i;
		NodeList children;
		switch (node.getNodeType()) {
		case 1:
			if (!node.hasChildNodes()) {
				return;
			} else {
				for (i = 0; i < deep; ++i) {
					pw.print(indent);
				}

				String nodeName = node.getNodeName();
				pw.print('<');
				pw.print(nodeName);
				NamedNodeMap nnm = node.getAttributes();
				Node n;
				if (nnm != null) {
					for (int i1 = 0; i1 < nnm.getLength(); ++i1) {
						n = nnm.item(i1);
						pw.print(' ');
						pw.print(n.getNodeName());
						pw.print("=\"");
						pw.print(n.getNodeValue());
						pw.print('"');
					}
				}

				if (node.hasChildNodes()) {
					children = node.getChildNodes();
					if (children.getLength() == 0) {
						pw.print('<');
						pw.print(nodeName);
						pw.println("/>");
						return;
					}

					if (children.getLength() == 1) {
						n = children.item(0);
						if (n.getNodeType() == 3) {
							String v = n.getNodeValue();
							if (v != null) {
								v = v.trim();
							}

							if (v != null && v.length() != 0) {
								pw.print('>');
								pw.print(v);
								pw.print("</");
								pw.print(nodeName);
								pw.println('>');
								return;
							}

							pw.println(" />");
							return;
						}
					}

					pw.println(">");

					for (i = 0; i < children.getLength(); ++i) {
						writeNode(children.item(i), pw, deep + 1);
					}

					for (i = 0; i < deep; ++i) {
						pw.print(indent);
					}

					pw.print("</");
					pw.print(nodeName);
					pw.println(">");
				} else {
					pw.println("/>");
				}

				return;
			}
		case 2:
		case 4:
		case 5:
		case 6:
		case 7:
		default:
			return;
		case 3:
			String value = node.getNodeValue().trim();
			if (value.length() == 0) {
				return;
			}

			writeIndent(pw, deep);

			for (i = 0; i < value.length(); ++i) {
				char c = value.charAt(i);
				switch (c) {
				case '"':
					pw.print("&quot;");
					break;
				case '&':
					pw.print("&amp;");
					break;
				case '\'':
					pw.print("&apos;");
					break;
				case '<':
					pw.print("&lt;");
					break;
				case '>':
					pw.print("&lt;");
					break;
				default:
					pw.print(c);
				}
			}

			pw.println();
			return;
		case 8:
			writeIndent(pw, deep);
			pw.print("<!--");
			pw.print(node.getNodeValue());
			pw.println("-->");
			return;
		case 9:
			pw.println(XML_HEAD);
			children = node.getChildNodes();

			for (i = 0; i < children.getLength(); ++i) {
				writeNode(children.item(i), pw, 0);
			}

		}
	}

	private Node findNode(String key) {
		Node ancestor = this.root;
		StringTokenizer st = new StringTokenizer(key, "/");

		Node n;
		label25: do {
			while (st.hasMoreTokens()) {
				String nodeName = st.nextToken();
				NodeList nl = ((Node) ancestor).getChildNodes();

				for (int i = 0; i < nl.getLength(); ++i) {
					n = nl.item(i);
					if (nodeName.equals(n.getNodeName())) {
						ancestor = n;
						continue label25;
					}
				}
			}

			return null;
		} while (st.hasMoreTokens());

		return n;
	}

	private Node createNode(String key) {
		Node ancestor = this.root;
		StringTokenizer st = new StringTokenizer(key, "/");

		Node n;
		label33: do {
			if (!st.hasMoreTokens()) {
				return null;
			}

			String nodeName = st.nextToken();
			NodeList nl = ((Node) ancestor).getChildNodes();

			for (int i = 0; i < nl.getLength(); ++i) {
				n = nl.item(i);
				if (nodeName.equals(n.getNodeName())) {
					ancestor = n;
					continue label33;
				}
			}

			while (true) {
				Node n1 = this.doc.createElement(nodeName);
				((Node) ancestor).appendChild(n1);
				ancestor = n1;
				if (!st.hasMoreTokens()) {
					return n1;
				}

				nodeName = st.nextToken();
			}
		} while (st.hasMoreTokens());

		return n;
	}

	private Node createNode(Node ancestor, String key) {
		NodeList nl;
		int i;
		label23: for (StringTokenizer st = new StringTokenizer(key, "/"); st.hasMoreTokens(); ancestor = nl.item(i)) {
			String nodeName = st.nextToken();
			nl = ancestor.getChildNodes();

			for (i = 0; i < nl.getLength(); ++i) {
				if (nodeName.equals(nl.item(i).getNodeName())) {
					continue label23;
				}
			}

			return null;
		}

		return ancestor;
	}

	public String get(String key, String def) {
		if (key == null) {
			throw new NullPointerException("parameter key is null");
		} else {
			Node node = this.findNode(key);
			if (node == null) {
				return def;
			} else {
				NodeList nl = node.getChildNodes();

				for (int i = 0; i < nl.getLength(); ++i) {
					if (nl.item(i).getNodeType() == 3) {
						return nl.item(i).getNodeValue().trim();
					}
				}

				node.appendChild(this.doc.createTextNode(def));
				return def;
			}
		}
	}

	public void put(String key, String value) {
		if (key == null) {
			throw new NullPointerException("parameter key is null");
		} else if (value == null) {
			throw new NullPointerException("parameter value is null");
		} else {
			value = value.trim();
			Node node = this.createNode(key);
			NodeList nl = node.getChildNodes();

			for (int i = 0; i < nl.getLength(); ++i) {
				Node child = nl.item(i);
				if (child.getNodeType() == 3) {
					String childValue = child.getNodeValue().trim();
					if (childValue.length() != 0) {
						if (childValue.equals(value)) {
							return;
						}

						child.setNodeValue(value);
						this.isDirty = true;
						return;
					}
				}
			}

			if (nl.getLength() == 0) {
				node.appendChild(this.doc.createTextNode(value));
			} else {
				Node f = node.getFirstChild();
				if (f.getNodeType() == 3) {
					f.setNodeValue(value);
				} else {
					node.insertBefore(this.doc.createTextNode(value), f);
				}
			}

			this.isDirty = true;
		}
	}

	public boolean getBoolean(String key, boolean def) {
		String str = String.valueOf(def);
		String resstr = this.get(key, str);
		Boolean resboolean = Boolean.valueOf(resstr);
		boolean result = resboolean;
		return result;
	}

	public int getInt(String key, int def) {
		String str = String.valueOf(def);
		String resstr = this.get(key, str);

		try {
			int result = Integer.parseInt(resstr);
			return result;
		} catch (NumberFormatException var8) {
			return def;
		}
	}

	public float getFloat(String key, float def) {
		String str = String.valueOf(def);
		String resstr = this.get(key, str);

		try {
			float result = Float.parseFloat(resstr);
			return result;
		} catch (NumberFormatException var8) {
			return def;
		}
	}

	public double getDouble(String key, double def) {
		String str = String.valueOf(def);
		String resstr = this.get(key, str);

		try {
			double result = Double.parseDouble(resstr);
			return result;
		} catch (NumberFormatException var11) {
			return def;
		}
	}

	public long getLong(String key, long def) {
		String str = String.valueOf(def);
		String resstr = this.get(key, str);

		try {
			long result = Long.parseLong(resstr);
			return result;
		} catch (NumberFormatException var11) {
			return def;
		}
	}

	public byte[] getByteArray(String key, byte[] def) {
		String str = new String(def);
		String resstr = this.get(key, str);
		byte[] result = resstr.getBytes();
		return result;
	}

	public void putBoolean(String key, boolean value) {
		String str = String.valueOf(value);

		try {
			this.put(key, str);
		} catch (RuntimeException var5) {
			throw var5;
		}
	}

	public void putInt(String key, int value) {
		String str = String.valueOf(value);

		try {
			this.put(key, str);
		} catch (RuntimeException var5) {
			throw var5;
		}
	}

	public void putFloat(String key, float value) {
		String str = String.valueOf(value);

		try {
			this.put(key, str);
		} catch (RuntimeException var5) {
			throw var5;
		}
	}

	public void putDouble(String key, double value) {
		String str = String.valueOf(value);

		try {
			this.put(key, str);
		} catch (RuntimeException var6) {
			throw var6;
		}
	}

	public void putLong(String key, long value) {
		String str = String.valueOf(value);

		try {
			this.put(key, str);
		} catch (RuntimeException var6) {
			throw var6;
		}
	}

	public void putByteArray(String key, byte[] value) {
		this.put(key, Base64.encode(value));
	}

	public void removeNode(String key) {
		Node node = this.findNode(key);
		if (node != null) {
			Node parentnode = node.getParentNode();
			if (parentnode != null) {
				parentnode.removeChild(node);
				this.isDirty = true;
			}

		}
	}

	public void clear(String key) {
		Node node = this.findNode(key);
		if (node == null) {
			throw new RuntimeException("InvalidName");
		} else {
			Node lastnode = null;

			while (node.hasChildNodes()) {
				lastnode = node.getLastChild();
				node.removeChild(lastnode);
			}

			if (lastnode != null) {
				this.isDirty = true;
			}

		}
	}

	public String[] childrenNames(String key) {
		Node node = this.findNode(key);
		if (node == null) {
			return new String[0];
		} else {
			NodeList nl = node.getChildNodes();
			LinkedList list = new LinkedList();

			for (int i = 0; i < nl.getLength(); ++i) {
				Node child = nl.item(i);
				if (child.getNodeType() == 1 && child.hasChildNodes()) {
					list.add(child.getNodeName());
				}
			}

			String[] ret = new String[list.size()];

			for (int i = 0; i < ret.length; ++i) {
				ret[i] = (String) list.get(i);
			}

			return ret;
		}
	}

	public boolean nodeExist(String key) {
		Node theNode = this.findNode(key);
		if (theNode == null) {
			return false;
		} else {
			return theNode.hasChildNodes();
		}
	}

	private void loadXMLParser() throws IOException {
		if (builder == null) {
			try {
				factory = DocumentBuilderFactory.newInstance();
				factory.setIgnoringComments(true);
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException var2) {
				throw new IOException(
						"XML Parser load error:".concat(String.valueOf(String.valueOf(var2.getLocalizedMessage()))));
			}
		}

	}

	public void load() throws IOException {
		this.loadXMLParser();

		try {
			InputSource is = new InputSource(new InputStreamReader((new URL(this.file)).openStream()));
			is.setEncoding(System.getProperty("file.encoding"));
			this.doc = builder.parse(is);
		} catch (SAXException var4) {
			var4.printStackTrace();
			String message = var4.getMessage();
			Exception e = var4.getException();
			if (e != null) {
				message = String.valueOf(String.valueOf(message)).concat(String
						.valueOf(String.valueOf("embedded exception:".concat(String.valueOf(String.valueOf(e))))));
			}

			throw new IOException("XML file parse error:".concat(String.valueOf(String.valueOf(message))));
		}

		this.root = this.doc.getDocumentElement();
		if (!"config".equals(this.root.getNodeName())) {
			throw new IOException("Config file format error, root node must be <config>");
		}
	}

	public void flush() throws IOException {
		if (this.isDirty) {
			String proc = (new URL(this.file)).getProtocol().toLowerCase();
			if (!proc.equalsIgnoreCase("file")) {
				throw new UnsupportedOperationException(
						"Unsupport write config URL on protocal ".concat(String.valueOf(String.valueOf(proc))));
			}

			String fileName = (new URL(this.file)).getPath();
			Debug.dump((new URL(this.file)).getPath());
			Debug.dump((new URL(this.file)).getFile());
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName), 2048);
			PrintWriter pw = new PrintWriter(bos);
			writeNode(this.doc, pw, 0);
			pw.flush();
			pw.close();
			this.isDirty = false;
		}

	}

	private String change(String str) throws IOException {
		if (str.indexOf(38) == -1 && str.indexOf(60) == -1 && str.indexOf(62) == -1) {
			return str;
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
			byte[] ba1 = new byte[] { 38, 97, 109, 112, 59 };
			byte[] ba2 = new byte[] { 38, 108, 116, 59 };
			byte[] ba3 = new byte[] { 38, 103, 116, 59 };

			byte temp;
			while ((temp = (byte) bis.read()) != -1) {
				switch (temp) {
				case 38:
					bos.write(ba1);
					break;
				case 60:
					bos.write(ba2);
					break;
				case 62:
					bos.write(ba3);
					break;
				default:
					bos.write(temp);
				}
			}

			return bos.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		Cfg c = new Cfg("app.xml", true);
		c.put("a/b", "汉字");
		c.put("c", "");
		c.put("a", "avalusaaaaaaaaae");
		c.flush();
		new Cfg("app.xml", true);
		System.out.println("Config file content:");
		BufferedReader in = new BufferedReader(new FileReader("app.xml"));

		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}

	}
}
