//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'gewt-chart' project.
// Copyright 2016 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
//
// Licensed under the Apache License, Version 3.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//	  http://www.apache.org/licenses/LICENSE-3.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package de.esoco.ewt.component;

import de.esoco.ewt.EWT;
import de.esoco.ewt.UserInterfaceContext;
import de.esoco.ewt.event.EventType;
import de.esoco.ewt.impl.gwt.GewtResources;
import de.esoco.ewt.impl.gwt.WidgetFactory;
import de.esoco.ewt.style.StyleData;

import de.esoco.lib.model.DataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartWidget;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.Properties;
import com.googlecode.gwt.charts.client.corechart.AreaChart;
import com.googlecode.gwt.charts.client.corechart.AreaChartOptions;
import com.googlecode.gwt.charts.client.corechart.BarChart;
import com.googlecode.gwt.charts.client.corechart.BarChartOptions;
import com.googlecode.gwt.charts.client.corechart.ColumnChart;
import com.googlecode.gwt.charts.client.corechart.ColumnChartOptions;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import com.googlecode.gwt.charts.client.corechart.LineChart;
import com.googlecode.gwt.charts.client.corechart.LineChartOptions;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import com.googlecode.gwt.charts.client.event.Event;
import com.googlecode.gwt.charts.client.event.EventHandler;
import com.googlecode.gwt.charts.client.event.HandlerRef;
import com.googlecode.gwt.charts.client.event.OnMouseOutEvent;
import com.googlecode.gwt.charts.client.event.OnMouseOutHandler;
import com.googlecode.gwt.charts.client.event.OnMouseOverEvent;
import com.googlecode.gwt.charts.client.event.OnMouseOverHandler;
import com.googlecode.gwt.charts.client.event.RegionClickEvent;
import com.googlecode.gwt.charts.client.event.RegionClickHandler;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.gauge.Gauge;
import com.googlecode.gwt.charts.client.gauge.GaugeOptions;
import com.googlecode.gwt.charts.client.geochart.GeoChart;
import com.googlecode.gwt.charts.client.geochart.GeoChartColorAxis;
import com.googlecode.gwt.charts.client.geochart.GeoChartOptions;
import com.googlecode.gwt.charts.client.options.CoreOptions;
import com.googlecode.gwt.charts.client.options.DisplayMode;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.Options;


/********************************************************************
 * A control component that displays graphical charts.
 *
 * @author eso
 */
public class Chart extends Component
{
	//~ Enums ------------------------------------------------------------------

	/********************************************************************
	 * The available positions for the chart legend.
	 */
	public enum ChartLegendPosition
	{
		TOP(LegendPosition.TOP), BOTTOM(LegendPosition.BOTTOM),
		LEFT(LegendPosition.IN), RIGHT(LegendPosition.RIGHT),
		NONE(LegendPosition.NONE);

		//~ Instance fields ----------------------------------------------------

		final LegendPosition rImplLegendPosition;

		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 *
		 * @param rGwtLegendPosition The corresponding GWT legend position
		 *                           constant
		 */
		private ChartLegendPosition(LegendPosition rGwtLegendPosition)
		{
			this.rImplLegendPosition = rGwtLegendPosition;
		}
	}

	/********************************************************************
	 * Enumeration of the available chart types.
	 */
	public enum ChartType
	{
		AREA()
		{
			@Override
			public ChartWidget<?> createChart()
			{
				return new AreaChart();
			}
			@Override
			public Options createOptions(boolean b3D)
			{
				return AreaChartOptions.create();
			}
		},
		BAR()
		{
			@Override
			public ChartWidget<?> createChart()
			{
				return new BarChart();
			}

			@Override
			public Options createOptions(boolean b3D)
			{
				return BarChartOptions.create();
			}
		},
		COLUMN()
		{
			@Override
			public ChartWidget<?> createChart()
			{
				return new ColumnChart();
			}
			@Override
			public Options createOptions(boolean b3D)
			{
				return ColumnChartOptions.create();
			}
		},
		GAUGE()
		{
			@Override
			public ChartWidget<?> createChart()
			{
				return new Gauge();
			}
			@Override
			public Options createOptions(boolean b3D)
			{
				return GaugeOptions.create();
			}
		},
		GEO_MAP()
		{
			@Override
			public ChartWidget<?> createChart()
			{
				return new InternalGeoChart();
			}

			@Override
			public GeoChartOptions createOptions(boolean b3D)
			{
				GeoChartColorAxis aColors  = GeoChartColorAxis.create();
				GeoChartOptions   aOptions = GeoChartOptions.create();

				aColors.setColors(new String[] { "68B7D4", "88D7F4" });

				aOptions.setColorAxis(aColors);
				aOptions.setDisplayMode(DisplayMode.REGIONS);
				aOptions.setDatalessRegionColor("E0F0FF");
				aOptions.hideLegend();

				return aOptions;
			}
		},
		LINE()
		{
			@Override
			public ChartWidget<?> createChart()
			{
				return new LineChart();
			}
			@Override
			public Options createOptions(boolean b3D)
			{
				return LineChartOptions.create();
			}
		},

		PIE()
		{
			@Override
			public ChartWidget<?> createChart()
			{
				return new PieChart();
			}

			@Override
			public PieChartOptions createOptions(boolean b3D)
			{
				PieChartOptions aOptions = PieChartOptions.create();

				aOptions.setIs3D(b3D);

				return aOptions;
			}
		};

		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 */
		private ChartType()
		{
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Create a new chart visualization instance.
		 *
		 * @return The new chart widget
		 */
		abstract ChartWidget<?> createChart();

		/***************************************
		 * Creates the GWT visualization data tables for this chart type. The
		 * default implementation returns a single {@link DataTable} created by
		 * {@link Chart#createDataTable(UserInterfaceContext, DataSet)}.
		 *
		 * @param  rContext The user interface context for resource expansion
		 * @param  rData    The data set to create the data table from
		 *
		 * @return The list of data tables for the chart type
		 */
		List<DataTable> createChartData(
			UserInterfaceContext rContext,
			DataSet<?>			 rData)
		{
			return Arrays.asList(createDataTable(rContext, rData));
		}

		/***************************************
		 * Creates a new instance of the options subclass for this chart type.
		 * The default implementation returns a new instance of {@link Options}
		 * and ignores the 3D flag.
		 *
		 * @param  b3D TRUE to display the chart in 3D (if supported)
		 *
		 * @return The options object
		 */
		Options createOptions(boolean b3D)
		{
			Options aOptions = CoreOptions.create();

			return aOptions;
		}
	}

	//~ Static fields/initializers ---------------------------------------------

	// static fields for chart API loading
	private static ChartLoader aChartLoader    = null;
	private static boolean     bChartApiLoaded = false;
	private static List<Chart> aDeferredCharts = new ArrayList<>();

	static
	{
		EWT.registerWidgetFactory(Chart.class, new ChartWidgetFactory(), false);
	}

	//~ Instance fields --------------------------------------------------------

	private ChartWidget<?> aChartWidget;

	private ChartType		    eChartType;
	private ChartLegendPosition eLegendPosition;

	private String  sBackgroundColor = null;
	private boolean bIs3D			 = false;
	private boolean bIsStacked		 = false;

	private DataSet<?>	    rChartData;
	private List<DataTable> aDataTables;
	private Options		    aOptions;

	//~ Static methods ---------------------------------------------------------

	/***************************************
	 * Creates a single chart data table from a data set.
	 *
	 * @param  rContext The user interface context for resource expansion
	 * @param  rDataSet The data set to create the data table from
	 *
	 * @return The new data table instance
	 */
	@SuppressWarnings("boxing")
	static DataTable createDataTable(
		UserInterfaceContext rContext,
		DataSet<?>			 rDataSet)
	{
		DataTable aDataTable = DataTable.create();

		int nRows = rDataSet.getRowCount();
		int nCols = rDataSet.getColumnCount();

		String sRowAxisLabel =
			rContext.expandResource(rDataSet.getRowAxisLabel());

		aDataTable.addColumn(ColumnType.STRING, sRowAxisLabel);

		aDataTable.addRows(nRows);

		for (int nRow = 0; nRow < nRows; nRow++)
		{
			String sLabel = rDataSet.getRowLabel(nRow);

			aDataTable.setValue(nRow, 0, rContext.expandResource(sLabel));
		}

		for (int nCol = 0; nCol < nCols; nCol++)
		{
			String sLabel =
				rContext.expandResource(rDataSet.getColumnLabel(nCol));

			if (sLabel != null)
			{
				aDataTable.addColumn(ColumnType.NUMBER, sLabel);
			}
			else
			{
				aDataTable.addColumn(ColumnType.NUMBER);
			}

			for (int nRow = 0; nRow < nRows; nRow++)
			{
				Object rValue = rDataSet.getValue(nRow, nCol);

				if (rValue instanceof Double)
				{
					aDataTable.setValue(nRow, nCol + 1, (Double) rValue);
				}
				else
				{
					aDataTable.setValue(nRow, nCol + 1, (Integer) rValue);
				}
			}
		}

		return aDataTable;
	}

	/***************************************
	 * Creates the chart data table from a data set.
	 *
	 * @param  rDataSet The data set to create the data table from
	 *
	 * @return The new data table instance
	 *
	 * @see    ChartType#NETWORK
	 */
	static List<DataTable> createNetworkChartTables(DataSet<String> rDataSet)
	{
		DataTable aDataTable     = DataTable.create();
		DataTable aRelationTable = DataTable.create();

		int nRows		 = rDataSet.getRowCount();
		int nRelationRow = 0;

		aDataTable.addRows(nRows);

		aDataTable.addColumn(ColumnType.STRING, "id");
		aDataTable.addColumn(ColumnType.STRING, "text");
		aDataTable.addColumn(ColumnType.STRING, "style");
		aDataTable.addColumn(ColumnType.STRING, "image");

		aRelationTable.addColumn(ColumnType.STRING, "from");
		aRelationTable.addColumn(ColumnType.STRING, "to");

		for (int nRow = 0; nRow < nRows; nRow++)
		{
			String sNodeId   = rDataSet.getValue(nRow, 0);
			String sParentId = rDataSet.getValue(nRow, 1);
			String sText     = rDataSet.getValue(nRow, 2);
			String sStyle    = rDataSet.getValue(nRow, 3);

			aDataTable.setValue(nRow, 0, sNodeId);
			aDataTable.setValue(nRow, 1, sText);

			if (sStyle.startsWith("image"))
			{
				String sImage = sStyle.substring(sStyle.indexOf(':') + 1);

				aDataTable.setValue(nRow, 2, "image");
				aDataTable.setValue(nRow, 3, sImage);
			}
			else
			{
				aDataTable.setValue(nRow, 2, sStyle);
			}

			if (sParentId != null)
			{
				aRelationTable.addRow();
				aRelationTable.setValue(nRelationRow, 0, sParentId);
				aRelationTable.setValue(nRelationRow, 1, sNodeId);
				nRelationRow++;
			}
		}

		return Arrays.asList(aDataTable, aRelationTable);
	}

	//~ Methods ----------------------------------------------------------------

	/***************************************
	 * Returns the background color.
	 *
	 * @return The background color
	 */
	@Override
	public final int getBackgroundColor()
	{
		return Integer.parseInt(sBackgroundColor, 16);
	}

	/***************************************
	 * Returns the chart type.
	 *
	 * @return The chart type
	 */
	public final ChartType getChartType()
	{
		return eChartType;
	}

	/***************************************
	 * Returns the legend position.
	 *
	 * @return The legend position
	 */
	public final ChartLegendPosition getLegendPosition()
	{
		return eLegendPosition;
	}

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	public void initWidget(Container rParent, StyleData rStyle)
	{
		super.initWidget(rParent, rStyle);

		setDefaultStyleName(GewtResources.INSTANCE.css().ewtChart());
	}

	/***************************************
	 * Returns TRUE if the chart is rendered in 3D.
	 *
	 * @return The 3D flag
	 */
	public final boolean is3D()
	{
		return bIs3D;
	}

	/***************************************
	 * Returns TRUE if the chart is displayed stacked.
	 *
	 * @return The stacked flag
	 */
	public final boolean isStacked()
	{
		return bIsStacked;
	}

	/***************************************
	 * @see de.esoco.ewt.component.Component#repaint()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void repaint()
	{
		if (!bChartApiLoaded)
		{
			aDeferredCharts.add(this);

			if (aChartLoader == null)
			{
				loadChartApi(ChartPackage.CORECHART, ChartPackage.GEOCHART);
			}
		}
		else
		{
			if (aChartWidget == null)
			{
				init();
			}

			if (aOptions instanceof GeoChartOptions)
			{
				// not working if set before...
				((GeoChartOptions) aOptions).hideLegend();
			}

			if (aChartWidget != null)
			{
				((ChartWidget<Options>) aChartWidget).draw(aDataTables.get(0),
														   aOptions);
			}
		}
	}

	/***************************************
	 * Sets the 3D display state of certain charts.
	 *
	 * @param b3D The 3D display flag
	 */
	public final void set3D(boolean b3D)
	{
		bIs3D = b3D;
	}

	/***************************************
	 * Sets the background color in the HTML color format.
	 *
	 * @param sColor The color definition
	 */
	public final void setBackgroundColor(String sColor)
	{
		sBackgroundColor = sColor;
	}

	/***************************************
	 * Sets the chart type.
	 *
	 * @param eChartType The new chart type
	 */
	public final void setChartType(ChartType eChartType)
	{
		this.eChartType = eChartType;
	}

	/***************************************
	 * Sets the data to be displayed by this chart. This method should be
	 * invoked after all other chart parameters have been set because it will
	 * trigger a redraw of the chart.
	 *
	 * @param rDataSet The new data
	 */
	public void setData(DataSet<?> rDataSet)
	{
		rChartData = rDataSet;
	}

	/***************************************
	 * Sets the legend position.
	 *
	 * @param ePosition The legend position
	 */
	public final void setLegendPosition(ChartLegendPosition ePosition)
	{
		eLegendPosition = ePosition;
	}

	/***************************************
	 * Sets the stacked D display state of certain charts.
	 *
	 * @param bStacked b3D The 3D display flag
	 */
	public final void setStacked(boolean bStacked)
	{
		bIsStacked = bStacked;
	}

	/***************************************
	 * Invoked when the chart javascript API has been fully loaded.
	 */
	void chartApiLoaded()
	{
		bChartApiLoaded = true;

		for (Chart rDeferredChart : aDeferredCharts)
		{
			if (rDeferredChart.aChartWidget == null)
			{
				rDeferredChart.init();
				rDeferredChart.repaint();
			}
		}

		aDeferredCharts = null;
	}

	/***************************************
	 * Initializes the chart.
	 */
	private void init()
	{
		HasWidgets rChartPanel = (HasWidgets) getWidget();

		rChartPanel.clear();
		aOptions = eChartType.createOptions(bIs3D);

		if (aOptions instanceof CoreOptions)
		{
			CoreOptions rCoreOptions = (CoreOptions) aOptions;

			if (sBackgroundColor != null)
			{
				rCoreOptions.setBackgroundColor(sBackgroundColor);
			}

			if (eLegendPosition != null)
			{
				Legend aLegend = Legend.create();

				aLegend.setPosition(eLegendPosition.rImplLegendPosition);
				rCoreOptions.setLegend(aLegend);
			}
		}

		if (aOptions instanceof AreaChartOptions)
		{
			((AreaChartOptions) aOptions).setIsStacked(bIsStacked);
		}
		else if (aOptions instanceof BarChartOptions)
		{
			((BarChartOptions) aOptions).setIsStacked(bIsStacked);
		}
		else if (aOptions instanceof ColumnChartOptions)
		{
			((ColumnChartOptions) aOptions).setIsStacked(bIsStacked);
		}

		aDataTables = eChartType.createChartData(getContext(), rChartData);

		DataTable aFirstTable = aDataTables.get(0);

		if (aFirstTable.getNumberOfRows() > 0 &&
			aFirstTable.getNumberOfColumns() > 0)
		{
			aChartWidget = eChartType.createChart();

			rChartPanel.add(aChartWidget);
			new ChartEventDispatcher().initEventDispatching(aChartWidget);
		}
		else
		{
			HTML aLabel =
				new HTML(getContext().expandResource("$lblNoEwtChartData"));

			aLabel.addStyleName(GewtResources.INSTANCE.css()
								.ewtNoChartDataLabel());
			rChartPanel.add(aLabel);
		}
	}

	/***************************************
	 * Loads the external chart API.
	 *
	 * @param rPackages The chart packages to load the API for
	 */
	private void loadChartApi(ChartPackage... rPackages)
	{
		aChartLoader = new ChartLoader(rPackages);

		aChartLoader.loadApi(new Runnable()
			{
				@Override
				public void run()
				{
					bChartApiLoaded = true;

					for (Chart rDeferredChart : aDeferredCharts)
					{
						if (rDeferredChart.aChartWidget == null)
						{
							rDeferredChart.init();
							rDeferredChart.repaint();
						}
					}

					aDeferredCharts = null;
				}
			});
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
			return new SimplePanel();
		}
	}

	/********************************************************************
	 * Re-implementation of {@link RegionClickHandler} due to event name typo in
	 * gwt-charts.
	 *
	 * @author eso
	 */
	public abstract class RegionSelectHandler implements EventHandler
	{
		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Invoked if a region is selected.
		 *
		 * @param rEvent The selection event
		 */
		public abstract void onRegionSelect(RegionSelectEvent rEvent);

		/***************************************
		 * {@inheritDoc}
		 */
		@Override
		public void dispatch(Properties rProperties)
		{
			onRegionSelect(new RegionSelectEvent(rProperties));
		}

		/***************************************
		 * {@inheritDoc}
		 */
		@Override
		public String getEventName()
		{
			return RegionSelectEvent.NAME;
		}
	}

	/********************************************************************
	 * Subclassing of {@link GeoChart} due to region click event name typo in
	 * gwt-charts.
	 *
	 * @author eso
	 */
	static class InternalGeoChart extends GeoChart
	{
		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Add region select handler.
		 *
		 * @param  rHandler The handler
		 *
		 * @return The handler reference
		 */
		public HandlerRef addRegionSelectHandler(RegionSelectHandler rHandler)
		{
			return addHandler(rHandler);
		}
	}

	/********************************************************************
	 * Re-implementation of {@link RegionClickEvent} due to event name typo in
	 * gwt-charts.
	 *
	 * @author eso
	 */
	static class RegionSelectEvent extends Event
	{
		//~ Static fields/initializers -----------------------------------------

		/** The event name. */
		public static String NAME = "regionClick";

		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new event.
		 *
		 * @param rProperties
		 */
		public RegionSelectEvent(Properties rProperties)
		{
			super(NAME, rProperties);
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Returns a string in ISO-3166 format describing the region clicked.
		 *
		 * @return a string in ISO-3166
		 */
		public String getRegion()
		{
			return properties.getString("region");
		}
	}

	/********************************************************************
	 * Dispatcher for list-specific events.
	 *
	 * @author eso
	 */
	class ChartEventDispatcher
	{
		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Initializes the event dispatching for a certain visualization widget.
		 *
		 * @param rChartWidget The visualization widget
		 */
		void initEventDispatching(ChartWidget<?> rChartWidget)
		{
			if (rChartWidget instanceof CoreChartWidget)
			{
				final CoreChartWidget<?> rChart =
					(CoreChartWidget<?>) rChartWidget;

				rChart.addSelectHandler(new SelectHandler()
					{
						@Override
						public void onSelect(SelectEvent rEvent)
						{
							notifyEventHandler(EventType.SELECTION);
						}
					});

				rChart.addOnMouseOverHandler(new OnMouseOverHandler()
					{
						@Override
						public void onMouseOver(OnMouseOverEvent rEvent)
						{
							notifyEventHandler(EventType.POINTER_ENTERED);
						}
					});

				rChart.addOnMouseOutHandler(new OnMouseOutHandler()
					{
						@Override
						public void onMouseOutEvent(OnMouseOutEvent rEvent)
						{
							notifyEventHandler(EventType.POINTER_EXITED);
						}
					});
			}
			else if (rChartWidget instanceof InternalGeoChart)
			{
				InternalGeoChart rGeoChart = (InternalGeoChart) rChartWidget;

				rGeoChart.addRegionSelectHandler(new RegionSelectHandler()
					{
						@Override
						public void onRegionSelect(RegionSelectEvent rEvent)
						{
							notifyEventHandler(EventType.SELECTION,
											   rEvent.getRegion(),
											   null);
						}
					});
			}
		}
	}
}
