//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'gewt-chart' project.
// Copyright 2016 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
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
import de.esoco.ewt.UserInterfaceContext;
import de.esoco.ewt.event.EventType;
import de.esoco.ewt.impl.gwt.GeoChart;
import de.esoco.ewt.impl.gwt.GeoChart.DisplayMode;
import de.esoco.ewt.impl.gwt.GewtResources;
import de.esoco.ewt.impl.gwt.WidgetFactory;
import de.esoco.ewt.style.StyleData;

import de.esoco.lib.model.DataSet;
import de.esoco.lib.model.StringDataSet;

import java.util.Arrays;
import java.util.List;

import com.chap.links.client.Network;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.AbstractDrawOptions;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.OnMouseOutHandler;
import com.google.gwt.visualization.client.events.OnMouseOverHandler;
import com.google.gwt.visualization.client.events.RegionClickHandler;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Visualization;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;


/********************************************************************
 * A control component that displays graphical charts.
 *
 * @author eso
 */
public class Chart extends Component implements Runnable
{
	//~ Enums ------------------------------------------------------------------

	/********************************************************************
	 * The available positions for the chart legend.
	 */
	public enum ChartLegendPosition
	{
		TOP(LegendPosition.TOP), BOTTOM(LegendPosition.BOTTOM),
		LEFT(LegendPosition.LEFT), RIGHT(LegendPosition.RIGHT),
		NONE(LegendPosition.NONE);

		//~ Instance fields ----------------------------------------------------

		final LegendPosition rGwtLegendPosition;

		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 *
		 * @param rGwtLegendPosition The corresponding GWT legend position
		 *                           constant
		 */
		private ChartLegendPosition(LegendPosition rGwtLegendPosition)
		{
			this.rGwtLegendPosition = rGwtLegendPosition;
		}
	}

	/********************************************************************
	 * Enumeration of the available chart types.
	 */
	public enum ChartType
	{
		AREA(AreaChart.PACKAGE)
		{
			@Override
			public Visualization<?> createChart(
				List<DataTable>		rData,
				AbstractDrawOptions rOptions)
			{
				return new AreaChart(rData.get(0), (Options) rOptions);
			}
		},
		BAR(BarChart.PACKAGE)
		{
			@Override
			public Visualization<?> createChart(
				List<DataTable>		rData,
				AbstractDrawOptions rOptions)
			{
				return new BarChart(rData.get(0), (Options) rOptions);
			}
		},
		COLUMN(ColumnChart.PACKAGE)
		{
			@Override
			public Visualization<?> createChart(
				List<DataTable>		rData,
				AbstractDrawOptions rOptions)
			{
				return new ColumnChart(rData.get(0), (Options) rOptions);
			}
		},
		GEO_MAP(GeoChart.PACKAGE)
		{
			@Override
			public Visualization<?> createChart(
				List<DataTable>		rData,
				AbstractDrawOptions rOptions)
			{
				return new GeoChart(rData.get(0), (GeoChart.Options) rOptions);
			}

			@Override
			public AbstractDrawOptions createOptions(boolean b3D)
			{
				GeoChart.Options aOptions = GeoChart.Options.create();

				aOptions.setDisplayMode(DisplayMode.REGIONS);
				aOptions.setDatalessRegionColor("FFD0C0");

				return aOptions;
			}
		},
		LINE(LineChart.PACKAGE)
		{
			@Override
			public Visualization<?> createChart(
				List<DataTable>		rData,
				AbstractDrawOptions rOptions)
			{
				return new LineChart(rData.get(0), (Options) rOptions);
			}
		},

		PIE(PieChart.PACKAGE)
		{
			@Override
			public Visualization<?> createChart(
				List<DataTable>		rData,
				AbstractDrawOptions rOptions)
			{
				return new PieChart(rData.get(0), (Options) rOptions);
			}

			@Override
			public AbstractDrawOptions createOptions(boolean b3D)
			{
				PieOptions aOptions = PieOptions.create();

				aOptions.set3D(b3D);

				return aOptions;
			}
		},

		/**
		 * Network chart data. Input data format must be a {@link StringDataSet}
		 * with rows of four columns containing the following node data:
		 *
		 * <ol>
		 *   <li>Node ID: a unique identifier for the node.</li>
		 *   <li>Parent ID: The ID of the parent node or NULL for a root node.
		 *     Can be an empty string if the parent is the same as for the
		 *     previous node.</li>
		 *   <li>Text: the text to be displayed for the node.</li>
		 *   <li>Style: the style name of the node.</li>
		 * </ol>
		 */
		NETWORK(null)
		{
			/***************************************
			 * @see ChartType#createChartData(UserInterfaceContext, DataSet)
			 */
			@Override
			@SuppressWarnings("unchecked")
			List<DataTable> createChartData(
				UserInterfaceContext rContext,
				DataSet<?>			 rData)
			{
				return createNetworkChartTables((DataSet<String>) rData);
			}

			/***************************************
			 * @see http://almende.github.io/chap-links-library/
			 */
			@Override
			public Visualization<?> createChart(
				List<DataTable>		rData,
				AbstractDrawOptions rOptions)
			{
				DataTable rNodes = rData.get(0);

				if (rData.size() == 1)
				{
					return new Network(rNodes, (Network.Options) rOptions);
				}
				else
				{
					return new Network(rNodes,
									   rData.get(1),
									   (Network.Options) rOptions);
				}
			}

			@Override
			@SuppressWarnings("boxing")
			public AbstractDrawOptions createOptions(boolean b3D)
			{
				Network.Options aOptions = Network.Options.create();

				aOptions.set("stabilize", true);

				return aOptions;
			}
		};

		//~ Instance fields ----------------------------------------------------

		private final String sGwtVisualizationPackage;

		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 *
		 * @param sPackage The GWT visualization package library
		 */
		ChartType(String sPackage)
		{
			sGwtVisualizationPackage = sPackage;
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Create a new chart visualization instance.
		 *
		 * @param  rData    The initial chart data
		 * @param  rOptions The initial chart options
		 *
		 * @return The new chart widget
		 */
		abstract Visualization<?> createChart(
			List<DataTable>		rData,
			AbstractDrawOptions rOptions);

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
		AbstractDrawOptions createOptions(boolean b3D)
		{
			Options aOptions = Options.create();

			return aOptions;
		}
	}

	//~ Static fields/initializers ---------------------------------------------

	static
	{
		EWT.registerWidgetFactory(Chart.class, new ChartWidgetFactory(), false);
	}

	//~ Instance fields --------------------------------------------------------

	private ChartType eChartType;

	private ChartLegendPosition eLegendPosition;
	private String			    sBackgroundColor = null;
	private boolean			    bIs3D			 = false;
	private boolean			    bIsStacked		 = false;

	private DataSet<?> rData;

	private Visualization<AbstractDrawOptions> aVisualization;
	private AbstractDrawOptions				   aOptions;

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
	public final String getBackgroundColor()
	{
		return sBackgroundColor;
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
	 * @see Runnable#run()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void run()
	{
		SimplePanel rPanel = (SimplePanel) getWidget();

		aOptions = eChartType.createOptions(bIs3D);

		if (aOptions instanceof Options)
		{
			Options rStandardOptions = (Options) aOptions;

			rStandardOptions.setIsStacked(bIsStacked);

			if (sBackgroundColor != null)
			{
				rStandardOptions.setBackgroundColor(sBackgroundColor);
			}

			if (eLegendPosition != null)
			{
				rStandardOptions.setLegend(eLegendPosition.rGwtLegendPosition);
			}
		}

		List<DataTable> aDataTables =
			eChartType.createChartData(getContext(), rData);

		DataTable aFirstTable = aDataTables.get(0);

		if (aFirstTable.getNumberOfRows() > 0 &&
			aFirstTable.getNumberOfColumns() > 0)
		{
			aVisualization =
				(Visualization<AbstractDrawOptions>) eChartType.createChart(aDataTables,
																			aOptions);
			rPanel.setWidget(aVisualization);
			new ChartEventDispatcher().initEventDispatching(aVisualization);
		}
		else
		{
			HTML aLabel =
				new HTML(getContext().expandResource("$lblNoEwtChartData"));

			aLabel.addStyleName(GewtResources.INSTANCE.css()
								.ewtNoChartDataLabel());
			rPanel.setWidget(aLabel);
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
		rData = rDataSet;

		if (eChartType.sGwtVisualizationPackage != null)
		{
			VisualizationUtils.loadVisualizationApi(this,
													eChartType.sGwtVisualizationPackage);
		}
		else
		{
			VisualizationUtils.loadVisualizationApi(this);
		}
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
		 * @param rVisualization The visualization widget
		 */
		void initEventDispatching(Visualization<?> rVisualization)
		{
			if (rVisualization instanceof CoreChart)
			{
				final CoreChart rChart = (CoreChart) rVisualization;

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
						public void onMouseOverEvent(OnMouseOverEvent rEvent)
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
			else if (rVisualization instanceof GeoChart)
			{
				GeoChart rGeoChart = (GeoChart) rVisualization;

				rGeoChart.addRegionClickHandler(new RegionClickHandler()
					{
						@Override
						public void onRegionClick(RegionClickEvent rEvent)
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
