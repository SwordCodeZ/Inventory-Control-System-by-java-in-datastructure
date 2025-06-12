package project;

import java.util.*;
import java.io.*;

public class BinaryTree {

    private static class Node {
        Item data;
        Node left, right;
        Node(Item data) { this.data = data; }
    }

    private Node root;

    public void insert(Item newItem) {
        Node newNode = new Node(newItem);
        if (root == null) {
            root = newNode;
            return;
        }
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node c = q.remove();
            if (c.left == null) { c.left = newNode; return; } else q.add(c.left);
            if (c.right == null) { c.right = newNode; return; } else q.add(c.right);
        }
    }

    public void delete(int itemNo) {
        if (root == null) return;
        if (root.left == null && root.right == null) {
            if (root.data.itemNo == itemNo) root = null;
            return;
        }

        Queue<Node> q = new LinkedList<>();
        q.add(root);
        Node target = null, last = null;

        while (!q.isEmpty()) {
            last = q.remove();
            if (last.data.itemNo == itemNo) target = last;
            if (last.left != null) q.add(last.left);
            if (last.right != null) q.add(last.right);
        }

        if (target != null) {
            target.data = last.data;
            deleteDeepest(last);
        }
    }

    private void deleteDeepest(Node d) {
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node c = q.remove();
            if (c.left != null) {
                if (c.left == d) { c.left = null; return; } else q.add(c.left);
            }
            if (c.right != null) {
                if (c.right == d) { c.right = null; return; } else q.add(c.right);
            }
        }
    }

    public Item search(int itemNo) {
        Queue<Node> q = new LinkedList<>();
        if (root != null) q.add(root);
        while (!q.isEmpty()) {
            Node c = q.remove();
            if (c.data.itemNo == itemNo) return c.data;
            if (c.left != null) q.add(c.left);
            if (c.right != null) q.add(c.right);
        }
        return null;
    }

    public List<Item> toList() {
        List<Item> list = new ArrayList<>();
        Queue<Node> q = new LinkedList<>();
        if (root != null) q.add(root);
        while (!q.isEmpty()) {
            Node c = q.remove();
            list.add(c.data);
            if (c.left != null) q.add(c.left);
            if (c.right != null) q.add(c.right);
        }
        return list;
    }

    public void saveToFile(String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            for (Item i : toList()) {
                fw.write(i.itemNo + "," + i.name + "," + i.price + "," + i.quantity + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int no = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int qty = Integer.parseInt(parts[3].trim());
                    insert(new Item(no, name, price, qty));
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
