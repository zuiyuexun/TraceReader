package com.panda.ui.menu;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.panda.ui.TraceFrame;
import com.panda.ui.list.ThreadListExt;
import com.panda.ui.tree.AbstractNode;
import com.panda.ui.tree.CallStackTree;
import com.panda.ui.tree.MethodsExtendTree;

public class TreePopupMenu extends JPopupMenu{
	JTree tree;
	private JMenuItem copy = null, hook = null, rename = null,fresh = null,showTime = null;
	TreePath focus;
	public TreePath getFocus() {
		return focus;
	}
	public void setFocus(TreePath focus) {
		this.focus = focus;
	}
	public TreePopupMenu(JTree thread){
		this.tree=thread;
	    this.add(copy = new JMenuItem("复制"));  
	    hook = new JMenuItem("添加hook");
	   // this.add(hook = new JMenuItem("添加hook"));  
	    this.add(rename = new JMenuItem("编辑")); 
	    this.add(showTime = new JMenuItem("显示时间"));
//	    this.add(fresh = new JMenuItem("刷新"));
	    copy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name="";
				if(focus.getPath().length>=1){
					AbstractNode node=(AbstractNode)focus.getLastPathComponent();
					if(node!=null){
						name=node.getM().getFullName();
					}
				}
				Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
		        Transferable tText = new StringSelection(name);  
//		        System.out.println(name);
		        clip.setContents(tText, null); 
			}
		});
	    hook.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(focus.getPath().length>=1){
					AbstractNode node=(AbstractNode)focus.getLastPathComponent();
					if(node!=null){
						node.getM().getFullName();
					}
				}
				
			}
		});
	    rename.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(focus.getPath().length>=1){
					AbstractNode node=(AbstractNode)focus.getLastPathComponent();
					if(node!=null){
						String rename=JOptionPane.showInputDialog("输入新名称：");
						if(rename==null){
							return;
						}
						TraceFrame f;
						if(tree instanceof CallStackTree){
							f=((CallStackTree)tree).getFrame();
						}else{
							f=((MethodsExtendTree)tree).getFrame();
						}
						new Thread(new Change(f,node.getM().getFullName(),rename)).start();
//						node.getM().setFullName(rename);
						
					}
				}
				
			}
		});
	    showTime.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(focus.getPath().length>=1){
					AbstractNode node=(AbstractNode)focus.getLastPathComponent();
					if(node!=null){
						node.setShowTime(true);;
					}
				}
				
			}
		});
	}
	class Change implements Runnable{
		TraceFrame f;
		String rename;
		String name;
		public Change(TraceFrame f,String name,String rename){
			this.f=f;
			this.rename=rename;
			this.name=name;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			f.rename(name,rename);
		}
		
	}
}
