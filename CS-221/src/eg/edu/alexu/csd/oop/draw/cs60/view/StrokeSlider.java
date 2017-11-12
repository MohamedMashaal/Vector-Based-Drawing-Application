package eg.edu.alexu.csd.oop.draw.cs60.view;

import javax.swing.JSlider;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class StrokeSlider extends JSlider {
	static final int STK_MIN = 0;
	static final int STK_MAX = 15;
	static final int STK_INIT = 2;

	private View view;

	public StrokeSlider(final View view) {
		super(JSlider.HORIZONTAL, STK_MIN, STK_MAX, STK_INIT);
		this.view = view;
		setPaintLabels(true);
		setPaintTicks(true);
		setMajorTickSpacing(10);
		setMinorTickSpacing(1);
		addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				view.getController().strokeChange(getValue());
			}
		});
	}

}
