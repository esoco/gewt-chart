//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'gewt-chart' project.
// Copyright 2017 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//	  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package de.esoco.ewt.component;

import de.esoco.ewt.EWT;
import de.esoco.ewt.impl.gwt.WidgetFactory;
import de.esoco.ewt.style.StyleData;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;


/********************************************************************
 * TODO: DOCUMENT ME!
 *
 * @author eso
 */
public class MapChart extends Chart
{
	//~ Static fields/initializers ---------------------------------------------

	static
	{
		EWT.registerWidgetFactory(MapChart.class,
								  new ChartWidgetFactory(),
								  false);
	}

	//~ Inner Classes ----------------------------------------------------------

	/********************************************************************
	 * Widget factory for this component.
	 *
	 * @author eso
	 */
	public static class ChartWidgetFactory implements WidgetFactory<Widget>
	{
		//~ Methods ------------------------------------------------------------

		/***************************************
		 * {@inheritDoc}
		 */
		@Override
		public Widget createWidget(Component rComponent, StyleData rStyle)
		{
			return new Datamap();
		}
	}

	/********************************************************************
	 * TODO: DOCUMENT ME!
	 *
	 * @author eso
	 */
	public static class Datamap extends FocusWidget
	{
		//~ Instance fields ----------------------------------------------------

		private JsDatamap aJsDatamap;

		//~ Constructors -------------------------------------------------------

		/***************************************
		 * TODO: `Description`
		 */
		public Datamap()
		{
			setElement(Document.get().createDivElement());
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * @see com.google.gwt.user.client.ui.Widget#onLoad()
		 */
		@Override
		protected void onLoad()
		{
			super.onLoad();

			JsDatamapOptions aOptions = new JsDatamapOptions();

			aOptions.element    = getElement();
			aOptions.responsive = true;

			aJsDatamap = new JsDatamap(aOptions);

			Window.addResizeHandler(new ResizeHandler()
				{
					@Override
					public void onResize(ResizeEvent rEvent)
					{
						EWT.log("Resize...");

						aJsDatamap.resize();
					}
				});
		}
	}
}
