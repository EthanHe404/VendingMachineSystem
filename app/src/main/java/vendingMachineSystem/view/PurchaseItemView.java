package vendingMachineSystem.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import vendingMachineSystem.controller.PurchaseItemState;
import vendingMachineSystem.controller.VendingMachineState;

public class PurchaseItemView extends AbstractView {

	PurchaseItemState state;
	LinkedList<PurchaseItemLine> itemsToPurchase;
	String[][] itemDataArray;
	ItemData itemData;
	JTable purchaseItemTable;
	
	public PurchaseItemView(PurchaseItemState state) {
		this.state = state;
		this.itemsToPurchase = new LinkedList<PurchaseItemLine>();
		itemDataArray = state.getItemData();
		itemData = new ItemData(itemDataArray);
	}
	
	private class ItemData {
		ArrayList<String> itemNames;
		Map<String, Integer> availableQty;
		Map<String, Float> prices;
		
		public ItemData (String[][] itemDataArray) {
			itemNames = new ArrayList<String>();
			availableQty = new HashMap<String, Integer>();
			prices = new HashMap<String, Float>();
			
			for (String[] line: itemDataArray) {
				String itemName = line[1];
				String quantity = line[2];
				String price = line[3];
				itemNames.add(itemName);
				availableQty.put(itemName, Integer.parseInt(quantity));
				prices.put(itemName, Float.parseFloat(price));
			}
		}
		
		public void addQty(String itemName, int qty) {
			int prevQty = availableQty.get(itemName);
			availableQty.put(itemName, prevQty + qty);
		}
		
		public int getQty(String itemName) {
			return availableQty.get(itemName);
		}
		
		public float getPrice(String itemName) {
			return prices.get(itemName);
		}
		
		public String[] getItemNames() {
			return (String[]) itemNames.toArray(new String[0]);
		}
	}
	
	private class PurchaseItemLine extends JPanel {

		String itemName;
		int quantity;
		float price;
		
		public PurchaseItemLine(String item, int quantity) {
			this.itemName = item;
			this.quantity = quantity;
			this.price = quantity * PurchaseItemView.this.itemData.getPrice(item);
		}
		
		public String getItemName() {
			return this.itemName;
		}
		
		public int getQty() {
			return this.quantity;
		}
		
		public void addQty(int qty) {
			this.quantity = this.quantity + qty;
			this.price = quantity * PurchaseItemView.this.itemData.getPrice(this.itemName);
		}
		
		public float getPrice() {
			return this.price;
		}
		
	}
	
	private class AddItemDialog extends JDialog {
		
		JComboBox items;
		JSpinner quantityField;
		SpinnerNumberModel numberModel;
		String selectedItem;
		int quantity;
		
		public AddItemDialog() {
			
			setSize(200, 200);
			setLayout(new BorderLayout());
			
			JLabel title = new JLabel("Add Item to Purchase", SwingConstants.CENTER);
			title.setVerticalAlignment(SwingConstants.CENTER);
			add(title, BorderLayout.NORTH);
			
			JPanel addItemPanel = new JPanel();
			
			String[] itemList = PurchaseItemView.this.itemData.getItemNames();
			items = new JComboBox(itemList);
			items.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					boolean timedout = PurchaseItemView.this.state.checkTransactionTimeout();
					if (timedout) {
						AddItemDialog.this.dispose();
						return;
					}
					String selectedItem = String.valueOf(items.getSelectedItem());
					if (AddItemDialog.this.selectedItem != selectedItem) {
						System.out.println(selectedItem);
						AddItemDialog.this.selectedItem = selectedItem;
						addItemPanel.remove(quantityField);
						AddItemDialog.this.addQtySpinner(addItemPanel, selectedItem);
			            AddItemDialog.this.revalidate();
					}
				}
				
			});
			addItemPanel.add(items);
			
			selectedItem = itemList[0];
			addQtySpinner(addItemPanel, String.valueOf(selectedItem));
            
            add(addItemPanel, BorderLayout.CENTER);
			
            JPanel buttonPanel = new JPanel();
            
			JButton addItemButton = new JButton("Add Item");
			addItemButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					boolean timedout = PurchaseItemView.this.state.checkTransactionTimeout();
					if (timedout) {
						AddItemDialog.this.dispose();
						return;
					}
					quantity = (Integer) numberModel.getValue();
					if (quantity > 0) {
						boolean hasItem = false;
						for (PurchaseItemLine line: PurchaseItemView.this.itemsToPurchase) {
							if (line.getItemName().equals(selectedItem)) {
								line.addQty(quantity);
								hasItem = true;
							}
						}
						if (!hasItem) {
							PurchaseItemView.this.itemsToPurchase.add(new PurchaseItemLine(selectedItem, quantity));							
						}
						PurchaseItemView.this.itemData.addQty(selectedItem, -quantity);						
					}
					AddItemDialog.this.dispose();
					PurchaseItemView.this.display();
				}
				
			});
			buttonPanel.add(addItemButton);
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					PurchaseItemView.this.state.checkTransactionTimeout();
					AddItemDialog.this.dispose();
				}
				
			});
			buttonPanel.add(cancelButton);
			add(buttonPanel, BorderLayout.SOUTH);
			
			setVisible(true);
			setAlwaysOnTop(true);
		}
		
		private void addQtySpinner(JPanel addItemPanel, String name) {
			numberModel = new SpinnerNumberModel(
				PurchaseItemView.this.itemData.getQty(name) > 0 ? new Integer(1): new Integer(0), //value
				new Integer(0), //min
				new Integer(PurchaseItemView.this.itemData.getQty(name)),
				new Integer(1)
			);
            quantityField = new JSpinner(numberModel);
            quantityField.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					boolean timedout = PurchaseItemView.this.state.checkTransactionTimeout();
					if (timedout) {
						AddItemDialog.this.dispose();
						return;
					}
				}
            	
            });
            addItemPanel.add(quantityField);
		}
		
	}
	
	private class PurchaseItemsTableModel extends AbstractTableModel {
		
		private String[] columnNames = { "Item", "Quantity", "Price", "" };
		private Object[][] data;
		
		public PurchaseItemsTableModel(LinkedList<PurchaseItemLine> itemsToPurchase) {
			float total = 0;
			for (PurchaseItemLine line: itemsToPurchase) {
				JButton deleteButton = new JButton("Delete Item");
				Object[] tableLine = {
					line.getItemName(),
					(Integer)line.getQty(),
					String.format("%.2f", (Float)line.getPrice()),
					deleteButton
				};
				if (data == null) {
					data = new Object[1][4];
					data[0] = tableLine;
				}
				else {
					data = appendToArray(data, tableLine);
				}
				total += line.getPrice();
			}
			if (data != null) {
				Object[] tableLine = {
					"",
					"Total",
					String.format("%.2f", (Float)total),
					null
				};
				data = appendToArray(data, tableLine);
			}
			return;
		}
		
		private Object[][] appendToArray(Object[][] data, Object[] newLine) {
			int oldLength = data.length;
			Object[][] newArray = new Object[oldLength + 1][data[0].length];
			int rowNumber = 0;
			for (Object[] row: data) {
				newArray[rowNumber] = row;
				rowNumber++;
			}
			newArray[newArray.length-1] = newLine;
			return newArray;
		}

		@Override
		public int getRowCount() {
			return data == null ? 0 : data.length;
		}
		
	    @Override
	    public String getColumnName(int column) {
	        return columnNames[column];
	    }

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data == null ? null : data[rowIndex][columnIndex];
		}
		
	}
	
	@Override
	public void display() {
		Window window = Window.getInstance();
		
		Panel p = new Panel();
		BoxLayout layout = new BoxLayout(p, BoxLayout.Y_AXIS);
		p.setLayout(layout);
		
		this.addMenu(p);
		this.addPurchaseItemPanel(p);
		this.addActionButtons(p);
		
		window.updateWindow(p);

	}
	
	private void addMenu(Panel p) {
		String[] names = {"Category", "Item", "Quantity", "Price"};
		JTable tab = new JTable(itemDataArray, names);
		JScrollPane tab_scroller = new JScrollPane(tab);
		tab_scroller.setPreferredSize(new Dimension(650, 100));
		p.add(tab_scroller);
	}
	
	private void addPurchaseItemPanel(Panel p) {
		Panel addItemPanel = new Panel();
		addItemPanel.setLayout(new FlowLayout());
		JLabel recLabel = new JLabel("Purchase Items");
		recLabel.setPreferredSize(new Dimension(650, 20));
		addItemPanel.add(recLabel, FlowLayout.LEFT);
		addItemPanel.setPreferredSize(new Dimension(650, 20));		
		p.add(addItemPanel);
		
		purchaseItemTable = new JTable(new PurchaseItemsTableModel(itemsToPurchase));
		purchaseItemTable.getColumn("").setCellRenderer(new ButtonRenderer());
		
		purchaseItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		    	
		        int row = purchaseItemTable.rowAtPoint(evt.getPoint());
		        int col = purchaseItemTable.columnAtPoint(evt.getPoint());
		        if(col == 3 && row != purchaseItemTable.getRowCount() - 1) {
					boolean timedout = PurchaseItemView.this.state.checkTransactionTimeout();
					if (timedout) {
						return;
					}
		        	PurchaseItemLine line = PurchaseItemView.this.itemsToPurchase.get(row);
					PurchaseItemView.this.itemData.addQty(line.getItemName(), line.getQty());
					PurchaseItemView.this.itemsToPurchase.remove(line);
					PurchaseItemView.this.display();
		        }
		    }
		});
		
		JScrollPane purchaseItemScroller = new JScrollPane(purchaseItemTable);
		purchaseItemScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		purchaseItemScroller.setPreferredSize(new Dimension(650, 100));
		
		for (PurchaseItemLine item: itemsToPurchase) {
			purchaseItemTable.add(item);
		}			
		
		p.add(purchaseItemScroller);
	}
	
	private class ButtonRenderer implements TableCellRenderer {

		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
			  return (JButton) value;
		  }
	}
	
	private void addPurchaseItemLine(Panel p) {
		new AddItemDialog();
		this.display();
	}

	private void addActionButtons(Panel p) {
		Panel buttonPanel = new Panel();
		FlowLayout flowLayout = new FlowLayout();
		buttonPanel.setLayout(flowLayout);
		
		JButton addPurchaseItemButton = new JButton("Add Item");
		addPurchaseItemButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean timedout = PurchaseItemView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}
				PurchaseItemView.this.addPurchaseItemLine(buttonPanel);
			}
		});
		buttonPanel.add(addPurchaseItemButton);
		
		JButton cashPayButton = new JButton("Pay with Cash");
		cashPayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean timedout = PurchaseItemView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}
				Map<String, Integer> itemsToPurchase = PurchaseItemView.this.getItemsToPurchase();
				PurchaseItemView.this.state.changeToCashPaymentPage(itemsToPurchase);
			}
			
		});
		buttonPanel.add(cashPayButton);
		
		JButton cardPayButton = new JButton("Pay with Card");
		cardPayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean timedout = PurchaseItemView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}
				Map<String, Integer> itemsToPurchase = PurchaseItemView.this.getItemsToPurchase();
				PurchaseItemView.this.state.changeToCardPaymentPage(itemsToPurchase);
			}
			
		});

		cardPayButton.setBounds( 170, 250, 150, 25 );
		buttonPanel.add(cardPayButton);
		
		JButton cancelButton;

		if(PurchaseItemView.this.state.getLoggedInStatus()){
			cancelButton = new JButton("Cancel Transaction & Return");
		}else{
			cancelButton = new JButton("Cancel Transaction");
		}

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PurchaseItemView.this.state.cancelTransaction();
			}
			
		});
		buttonPanel.add(cancelButton);
		
		p.add(buttonPanel);
	}
	
	private Map<String, Integer> getItemsToPurchase() {
		Map<String, Integer> itemMap = new HashMap<String, Integer>();
		for (PurchaseItemLine itemLine: itemsToPurchase) {
			String itemName = itemLine.getItemName();
			int qty = itemLine.getQty();
			if (itemMap.containsKey(itemName)) {
				int prevQty = itemMap.get(itemName);
				itemMap.put(itemName, prevQty + qty);
			}
			else {
				itemMap.put(itemName, qty);
			}
		}
		return itemMap;
	}

}
