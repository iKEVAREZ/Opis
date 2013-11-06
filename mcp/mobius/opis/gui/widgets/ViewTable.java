package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.helpers.UIException;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class ViewTable extends WidgetBase{

	public class Cell extends WidgetBase{
		float    fontSize = 1.0f;
		
		public Cell(IWidget parent, String text, WAlign align, float fontSize){
			super(parent);
			
			this.fontSize = fontSize;
			
			//this.addWidget("Crop", new LayoutCropping(null)).setGeometry(new WidgetGeometry(50.0, 50.0, 90.0, 100.0, CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
			
			switch(align){
			case CENTER:
				//this.getWidget("Crop").addWidget("Text", new LabelFixedFont(null, text))
				this.addWidget("Text", new LabelScalable(null, text))
				    .setGeometry(new WidgetGeometry(50.0, 50.0, 100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
				((LabelScalable)this.getWidget("Text")).setScale(this.fontSize);
				break;
			case LEFT:
				//this.getWidget("Crop").addWidget("Text", new LabelFixedFont(null, text))
				this.addWidget("Text", new LabelScalable(null, text))
				    .setGeometry(new WidgetGeometry(5.0, 50.0, 95.0, 100.0,  CType.RELXY, CType.RELXY, WAlign.LEFT,   WAlign.CENTER));
				((LabelScalable)this.getWidget("Text")).setScale(this.fontSize);				
				break;
			case RIGHT: 
				//this.getWidget("Crop").addWidget("Text", new LabelFixedFont(null, text))
				this.addWidget("Text", new LabelScalable(null, text))
				    .setGeometry(new WidgetGeometry(5.0, 50.0, 95.0, 100.0,  CType.RELXY, CType.RELXY, WAlign.RIGHT,  WAlign.CENTER));
				((LabelScalable)this.getWidget("Text")).setScale(this.fontSize);				
				break;
			default:
				throw new UIException(String.format("Unexpected align value : %s", align));
			}
		}
		
		@Override
		public void draw(Point pos){}
		

		public void setFontSize(float size){
			this.fontSize = size;
		}			
		
	}

	public class Row extends WidgetBase{
		int ncolumns = -1;
		double[] widths;
		String[] texts;
		WAlign[] aligns;
		float    fontSize = 1.0f;
		boolean  init = false;
		int bgcolor1 = 0xff505050;
		int bgcolor2 = 0xff505050;
		Object obj;
		
		public Row(IWidget parent, float fontSize){
			super(parent);

			this.fontSize = fontSize;
			
			this.addWidget("Background", new LayoutBase(null)).setGeometry(new WidgetGeometry(50.0, 50.0, 100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
			((LayoutBase)this.getWidget("Background")).setBackgroundColors(this.bgcolor1, this.bgcolor2);
			
		}
		
		public Row attachObject(Object obj){
			this.obj = obj;
			return this;
		}

		public Object getObject(){
			return this.obj;
		}
		
		public void setFontSize(float size){
			this.fontSize = size;
		}		
		
		public Row setColumnsWidth(double... widths){
			if (this.ncolumns == -1)
				this.ncolumns = widths.length;
			else if(this.ncolumns != widths.length){
				throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", this.ncolumns, widths.length));
			}
			
			this.widths = widths;
			
			return this;
		}
		
		public Row setColumnsText(String...strings ){
			if (this.ncolumns == -1)
				this.ncolumns = strings.length;
			else if(this.ncolumns != strings.length){
				throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", this.ncolumns, strings.length));
			}
			
			this.texts = strings;
			
			return this;			
		}
		
		public Row setColumnsAlign(WAlign... aligns ){
			if (this.ncolumns == -1)
				this.ncolumns = aligns.length;
			else if(this.ncolumns != aligns.length){
				throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", this.ncolumns, aligns.length));
			}
			
			this.aligns = aligns;
			
			return this;			
		}		
		
		public Row setColors(int bg1, int bg2){
			this.bgcolor1 = bg1;
			this.bgcolor2 = bg2;
			((LayoutBase)this.getWidget("Background")).setBackgroundColors(this.bgcolor1, this.bgcolor2);
			return this;
		}
		
		@Override
		public void draw(){
			if (!init)
			{
				double currentOffset = 0.0;
				for (int i = 0; i < this.ncolumns; i++){
					if (!this.widgets.containsKey(String.format("Cell_%02d", i))){
						Cell cell = (Cell)(this.addWidget(String.format("Cell_%02d", i), new Cell(null, this.texts[i], this.aligns[i], this.fontSize)));
						cell.setGeometry(new WidgetGeometry(currentOffset, 50.0, this.widths[i], 100.0, CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.CENTER));
						currentOffset += this.widths[i];
					}
				}
				init = true;
			}
			super.draw();
		}
		
		@Override
		public void draw(Point pos){}
	}

	int ncolumns = -1;
	int nrows    = 0;
	double[] widths;
	String[] texts;
	WAlign[] aligns;
	float    fontSize = 1.0f;
	boolean  init = false;		
	int rowColorOdd  = 0x50505050;
	int rowColorEven = 0x50808080;
	
	public ViewTable(IWidget parent){
		super(parent);
		this.addWidget("Titles",   new Row(null, this.fontSize)).setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 16.0, CType.REL_X, CType.REL_X, WAlign.LEFT, WAlign.TOP));
		((Row)this.getWidget("Titles")).setColors(0x00000000, 0x00000000);
		this.addWidget("Viewport", new ViewportScrollable(null)).setGeometry(new WidgetGeometry(0.0, 16.0, 100.0, 90.0, CType.REL_X, CType.RELXY, WAlign.LEFT, WAlign.TOP));
		((ViewportScrollable)(this.getWidget("Viewport"))).attachWidget(new LayoutBase(null)).setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 0.0, CType.RELXY, CType.REL_X, WAlign.LEFT, WAlign.TOP));
		
	}
	
	@Override
	public void draw(Point pos){}
	
	public ViewTable setColumnsWidth(double... widths){
		if (this.ncolumns == -1)
			this.ncolumns = widths.length;
		else if(this.ncolumns != widths.length){
			throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", this.ncolumns, widths.length));
		}
		
		this.widths = widths;
		((Row)this.getWidget("Titles")).setColumnsWidth(widths);
		return this;
	}
	
	public ViewTable setRowColors(int even, int odd){
		this.rowColorEven = even;
		this.rowColorOdd  = odd;
		return this;
	}
	
	public ViewTable setColumnsTitle(String...strings ){
		if (this.ncolumns == -1)
			this.ncolumns = strings.length;
		else if(this.ncolumns != strings.length){
			throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", this.ncolumns, strings.length));
		}
		
		this.texts = strings;
		((Row)this.getWidget("Titles")).setColumnsText(strings);
		
		return this;			
	}
	
	public ViewTable setColumnsAlign(WAlign... aligns ){
		if (this.ncolumns == -1)
			this.ncolumns = aligns.length;
		else if(this.ncolumns != aligns.length){
			throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", this.ncolumns, aligns.length));
		}
		
		this.aligns = aligns;
		((Row)this.getWidget("Titles")).setColumnsAlign(aligns);
		
		return this;			
	}			
	
	public ViewTable addRow(String...strings ){
		this.addRow(null, strings);
		return this;
	}
		
	public ViewTable addRow(Object obj, String...strings){		
		IWidget tableLayout = ((ViewportScrollable)(this.getWidget("Viewport"))).getAttachedWidget();
		tableLayout.setSize(100.0, (this.nrows + 1) * 16);
		
		Row newRow = (Row)new Row(null, this.fontSize);
		newRow.setColumnsWidth(this.widths);
		newRow.setColumnsText(strings);
		newRow.setColumnsAlign(this.aligns);
		if(this.nrows % 2 == 1)
			newRow.setColors(this.rowColorOdd, this.rowColorOdd);
		else
			newRow.setColors(this.rowColorEven, this.rowColorEven);
		newRow.setGeometry(new WidgetGeometry(0.0, 16*this.nrows, 100.0, 16, CType.REL_X, CType.REL_X, WAlign.LEFT, WAlign.TOP));
		newRow.attachObject(obj);
		
		tableLayout.addWidget(String.format("Row_%03d", this.nrows), newRow);
		
		this.nrows += 1;
		
		return this;
	}
	
	public Row getRow(double x, double y){
		return (Row)this.getWidgetAtLayer(x, y, 4);
	}
	
	public void setFontSize(float size){
		this.fontSize = size;
	}
	
}
