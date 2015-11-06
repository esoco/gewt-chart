//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'gewt-chart' project.
// Copyright 2015 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
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
package de.esoco.ewt.impl.gwt;

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDrawOptions;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.Handler;
import com.google.gwt.visualization.client.events.ReadyHandler;
import com.google.gwt.visualization.client.events.RegionClickHandler;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.events.ZoomOutHandler;
import com.google.gwt.visualization.client.visualizations.Visualization;


/********************************************************************
 * Simple wrapper for the GeoChart from google-visualization API.
 *
 * @author L. Plotnicki <lukasz.plotnicki@med.uni-heidelberg.de> Created with
 *         IntelliJ IDEA. Date: 09.07.12 Time: 16:42
 */
public class GeoChart extends Visualization<GeoChart.Options>
{
	//~ Enums ------------------------------------------------------------------

	/********************************************************************
	 * How to display data on the map.
	 */
	public static enum DisplayMode
	{
		/**
		 * Put markers on the map, changing size and color according to the
		 * number given.
		 */
		MARKERS,

		/** Color regions inside the map according to the number given. */
		REGIONS
	}

	//~ Static fields/initializers ---------------------------------------------

	/** Package name */
	public static final String PACKAGE = "geochart";

	//~ Constructors -----------------------------------------------------------

	/***************************************
	 * Creates a new instance.
	 *
	 * @param rData
	 * @param rOptions
	 */
	public GeoChart(AbstractDataTable rData, Options rOptions)
	{
		super(rData, rOptions);
	}

	//~ Methods ----------------------------------------------------------------

	/***************************************
	 * Add a ready event handler.
	 *
	 * @param rHandler
	 */
	public void addReadyHandler(ReadyHandler rHandler)
	{
		Handler.addHandler(this, "drawingDone", rHandler);
	}

	/***************************************
	 * Add a region click event handler.
	 *
	 * @param rHandler
	 */
	public void addRegionClickHandler(RegionClickHandler rHandler)
	{
		Handler.addHandler(this, "regionClick", rHandler);
	}

	/***************************************
	 * Add a select event handler.
	 *
	 * @param rHandler
	 */
	public final void addSelectHandler(SelectHandler rHandler)
	{
		Selection.addSelectHandler(this, rHandler);
	}

	/***************************************
	 * Add a zoom out event handler.
	 *
	 * @param rHandler
	 */
	public void addZoomOutHandler(ZoomOutHandler rHandler)
	{
		Handler.addHandler(this, "zoomOut", rHandler);
	}

	/***************************************
	 * Returns the selections.
	 *
	 * @return The selections
	 */
	public final JsArray<Selection> getSelections()
	{
		return Selection.getSelections(this);
	}

	/***************************************
	 * Sets the selections.
	 *
	 * @param rSelections The new selections
	 */
	public final void setSelections(JsArray<Selection> rSelections)
	{
		Selection.setSelections(this, rSelections);
	}

	/***************************************
	 * Creates the JavaScript object.
	 *
	 * @param  rParent The parent DOM element
	 *
	 * @return The JSO
	 */
	@Override
	protected native JavaScriptObject createJso(Element rParent)
	/*-{
		return new $wnd.google.visualization.GeoChart(rParent);
	}-*/;

	//~ Inner Classes ----------------------------------------------------------

	/********************************************************************
	 * Options for drawing the chart.
	 */
	public static class Options extends AbstractDrawOptions
	{
		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 */
		protected Options()
		{
		}

		//~ Static methods -----------------------------------------------------

		/***************************************
		 * Create the options.
		 *
		 * @return The options
		 */
		public static Options create()
		{
			return JavaScriptObject.createObject().cast();
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Sets the background color.
		 *
		 * @param sBackgroundColor The new background color
		 */
		public final native void setBackgroundColor(String sBackgroundColor)
		/*-{
			this.backgroundColor = sBackgroundColor;
		}-*/;

		/***************************************
		 * Sets the color axis.
		 *
		 * @param rColors The new color axis
		 */
		public final void setColorAxis(String... rColors)
		{
			GeoChartColorAxis a = GeoChartColorAxis.create();

			a.setColors(ArrayHelper.toJsArrayString(rColors));
			setColorAxis(a);
		}

		/***************************************
		 * Sets the dataless region color.
		 *
		 * @param sDatalessRegionColor The new dataless region color
		 */
		public final native void setDatalessRegionColor(
			String sDatalessRegionColor)
		/*-{
			this.datalessRegionColor = sDatalessRegionColor;
		}-*/;

		/***************************************
		 * Sets the display mode.
		 *
		 * @param eMode The new display mode
		 */
		public final void setDisplayMode(DisplayMode eMode)
		{
			setDisplayMode(eMode.name().toLowerCase());
		}

		/***************************************
		 * Sets the enable region interactivity.
		 *
		 * @param bEnableRegionInteractivity The new enable region interactivity
		 */
		public final native void setEnableRegionInteractivity(
			boolean bEnableRegionInteractivity)
		/*-{
			this.enableRegionInteractivity = bEnableRegionInteractivity;
		}-*/;

		/***************************************
		 * Sets the height.
		 *
		 * @param nHeight The new height
		 */
		public final native void setHeight(int nHeight)
		/*-{
			this.height = nHeight;
		}-*/;

		/***************************************
		 * Sets the keep aspect ratio.
		 *
		 * @param bKeepAspectRatio The new keep aspect ratio
		 */
		public final native void setKeepAspectRatio(boolean bKeepAspectRatio)
		/*-{
			this.keepAspectRatio = bKeepAspectRatio;
		}-*/;

		/***************************************
		 * Sets the magnifying glass.
		 *
		 * @param bEnabled    The enabled state
		 * @param fZoomFactor The zoom factor
		 */
		public final void setMagnifyingGlass(
			boolean bEnabled,
			double  fZoomFactor)
		{
			MagnifyingGlass aMagnifyingGlass = MagnifyingGlass.create();

			aMagnifyingGlass.setEnabled(bEnabled);
			aMagnifyingGlass.setZoomFactor(fZoomFactor);
			setMagnifyingGlass(aMagnifyingGlass);
		}

		/***************************************
		 * Sets the marker opacity.
		 *
		 * @param fMarkerOpacity The new marker opacity
		 */
		public final native void setMarkerOpacity(double fMarkerOpacity)
		/*-{
			this.markerOpacity = fMarkerOpacity;
		}-*/;

		/***************************************
		 * Sets the region.
		 *
		 * @param sRegion The new region
		 */
		public final native void setRegion(String sRegion)
		/*-{
			this.region = sRegion;
		}-*/;

		/***************************************
		 * Sets the size axis.
		 *
		 * @param nMaxSize Maximum radius of the largest possible bubble in
		 *                 pixels
		 * @param nMinSize Minimum radius of the smallest possible bubble in
		 *                 pixels
		 */
		public final void setSizeAxis(int nMaxSize, int nMinSize)
		{
			SizeAxis aAxis = SizeAxis.create();

			aAxis.setMaxSize(nMaxSize);
			aAxis.setMinSize(nMinSize);
			setSizeAxis(aAxis);
		}

		/***************************************
		 * Sets the size axis.
		 *
		 * @param nMaxSize  Maximum radius of the largest possible bubble in
		 *                  pixels
		 * @param nMinSize  Minimum radius of the smallest possible bubble in
		 *                  pixels
		 * @param fMaxValue Maximum value of size column in chart data The size
		 *                  value (as appears in the chart data) to be mapped to
		 *                  sizeAxis.maxSize. Larger values will be cropped to
		 *                  this value.
		 * @param fMinValue Minimum value of size column in chart data The size
		 *                  value (as appears in the chart data) to be mapped to
		 *                  sizeAxis.minSize. Smaller values will be cropped to
		 *                  this value.
		 */
		public final void setSizeAxis(int    nMaxSize,
									  int    nMinSize,
									  double fMaxValue,
									  double fMinValue)
		{
			SizeAxis aAxis = SizeAxis.create();

			aAxis.setMaxSize(nMaxSize);
			aAxis.setMaxValue(fMaxValue);
			aAxis.setMinSize(nMinSize);
			aAxis.setMinValue(fMinValue);
			setSizeAxis(aAxis);
		}

		/***************************************
		 * Sets the width.
		 *
		 * @param nWidth The new width
		 */
		public final native void setWidth(int nWidth)
		/*-{
			this.width = nWidth;
		}-*/;

		/***************************************
		 * Sets the color axis.
		 *
		 * @param rColorAxis The new color axis
		 */
		private final native void setColorAxis(GeoChartColorAxis rColorAxis)
		/*-{
			this.colorAxis = rColorAxis;
		}-*/;

		/***************************************
		 * Sets the display mode.
		 *
		 * @param sMode The new display mode
		 */
		private native void setDisplayMode(String sMode)
		/*-{
			this.displayMode = sMode;
		}-*/;

		/***************************************
		 * Sets the magnifying glass.
		 *
		 * @param rMagnifyingGlass The new magnifying glass
		 */
		private final native void setMagnifyingGlass(
			MagnifyingGlass rMagnifyingGlass)
		/*-{
			this.magnifyingGlass = rMagnifyingGlass;
		}-*/;

		/***************************************
		 * Sets the size axis.
		 *
		 * @param rSizeAxis The new size axis
		 */
		private final native void setSizeAxis(SizeAxis rSizeAxis)
		/*-{
			this.sizeAxis = rSizeAxis;
		}-*/;
	}

	/********************************************************************
	 * Color axis.
	 */
	private static class GeoChartColorAxis extends JavaScriptObject
	{
		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 */
		protected GeoChartColorAxis()
		{
		}

		//~ Static methods -----------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 *
		 * @return The new object
		 */
		public static GeoChartColorAxis create()
		{
			return createObject().cast();
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Sets the colors.
		 *
		 * @param rColors The new colors
		 */
		public final native void setColors(JsArrayString rColors)
		/*-{
			this.colors = rColors;
		}-*/;

		/***************************************
		 * Sets the max value.
		 *
		 * @param fMaxValue The new max value
		 */
		public final native void setMaxValue(double fMaxValue)
		/*-{
			this.maxValue = fMaxValue;
		}-*/;

		/***************************************
		 * Sets the minimum value.
		 *
		 * @param fMinValue The new minimum value
		 */
		public final native void setMinValue(double fMinValue)
		/*-{
			this.minValue = fMinValue;
		}-*/;

		/***************************************
		 * Sets the values.
		 *
		 * @param rValues The new values
		 */
		public final native void setValues(JsArrayNumber rValues)
		/*-{
			this.values = rValues;
		}-*/;
	}

	/********************************************************************
	 * The Magnifying glass.
	 */
	private static class MagnifyingGlass extends JavaScriptObject
	{
		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 */
		protected MagnifyingGlass()
		{
		}

		//~ Static methods -----------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 *
		 * @return The new object
		 */
		public static MagnifyingGlass create()
		{
			return createObject().cast();
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Sets the enabled state.
		 *
		 * @param bEnabled The new enabled state
		 */
		public final native void setEnabled(boolean bEnabled)
		/*-{
			this.enable = bEnabled;
		}-*/;

		/***************************************
		 * Sets the zoom factor.
		 *
		 * @param fZoomFactor The new zoom factor
		 */
		public final native void setZoomFactor(double fZoomFactor)
		/*-{
			this.zoomFactor = fZoomFactor;
		}-*/;
	}

	/********************************************************************
	 * An object with members to configure how values are associated with bubble
	 * size.
	 */
	private static class SizeAxis extends JavaScriptObject
	{
		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 */
		protected SizeAxis()
		{
		}

		//~ Static methods -----------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 *
		 * @return The new object
		 */
		public static SizeAxis create()
		{
			return createObject().cast();
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Sets the maximum radius of the largest possible bubble, in pixels.
		 *
		 * @param nMaxSize maximum radius of the largest possible bubble, in
		 *                 pixels
		 */
		public final native void setMaxSize(int nMaxSize)
		/*-{
			this.maxSize = nMaxSize;
		}-*/;

		/***************************************
		 * Sets the size value (as appears in the chart data) to be mapped to
		 * sizeAxis.maxSize. Larger values will be cropped to this value.
		 *
		 * @param nMaxValue
		 */
		public final native void setMaxValue(double nMaxValue)
		/*-{
			this.maxValue = nMaxValue;
		}-*/;

		/***************************************
		 * Sets the minimum radius of the smallest possible bubble, in pixels.
		 *
		 * @param nMinSize minimum radius of the smallest possible bubble, in
		 *                 pixels
		 */
		public final native void setMinSize(int nMinSize)
		/*-{
			this.minSize = nMinSize;
		}-*/;

		/***************************************
		 * Sets the size value (as appears in the chart data) to be mapped to
		 * sizeAxis.minSize. Smaller values will be cropped to this value.
		 *
		 * @param fMinValue
		 */
		public final native void setMinValue(double fMinValue)
		/*-{
			this.minValue = fMinValue;
		}-*/;
	}
}
