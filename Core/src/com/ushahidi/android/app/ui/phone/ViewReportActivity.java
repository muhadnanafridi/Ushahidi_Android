/**
 ** Copyright (c) 2010 Ushahidi Inc
 ** All rights reserved
 ** Contact: team@ushahidi.com
 ** Website: http://www.ushahidi.com
 **
 ** GNU Lesser General Public License Usage
 ** This file may be used under the terms of the GNU Lesser
 ** General Public License version 3 as published by the Free Software
 ** Foundation and appearing in the file LICENSE.LGPL included in the
 ** packaging of this file. Please review the following information to
 ** ensure the GNU Lesser General Public License version 3 requirements
 ** will be met: http://www.gnu.org/licenses/lgpl.html.
 **
 **
 ** If you have questions regarding the use of this file, please contact
 ** Ushahidi developers at team@ushahidi.com.
 **
 **/

package com.ushahidi.android.app.ui.phone;

import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ushahidi.android.app.R;
import com.ushahidi.android.app.activities.BaseMapViewActivity;
import com.ushahidi.android.app.entities.Category;
import com.ushahidi.android.app.models.ListReportModel;
import com.ushahidi.android.app.models.ViewReportModel;
import com.ushahidi.android.app.views.ViewReportView;

/**
 * @author eyedol
 */
public class ViewReportActivity extends
		BaseMapViewActivity<ViewReportView, ViewReportModel> {

	private ListReportModel reports;

	private List<ListReportModel> report;

	private int position;

	private int categoryId;

	private int reportId;

	public ViewReportActivity() {
		super(ViewReportView.class, R.layout.view_report, R.menu.view_report,
				R.id.loc_map);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		reports = new ListReportModel();
		view = new ViewReportView(this);

		this.categoryId = getIntent().getExtras().getInt("category", 0);
		this.position = getIntent().getExtras().getInt("id", 0);

		if (categoryId > 0) {
			reports.loadReportByCategory(categoryId);
		} else {
			reports.load();
		}
		initReport(this.position);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		} else if (item.getItemId() == R.id.menu_forward) {

			if (report != null) {
				position++;
				if (!(position > (report.size() - 1))) {
					initReport(position);

				} else {
					position = report.size() - 1;
				}
			}
			return true;

		} else if (item.getItemId() == R.id.menu_backward) {

			if (report != null) {
				position--;
				if ((position < (report.size() - 1)) && (position != -1)) {
					initReport(position);
				} else {
					position = 0;
				}
			}
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	private String fetchCategories(int reportId) {
		StringBuilder categories = new StringBuilder();
		for (Category category : reports.getCategoriesByReportId(reportId)) {
			if (category.getCategoryTitle().length() > 0) {
				categories.append(category.getCategoryTitle() + "|");
			}

		}

		// delete the last |
		if (categories.length() > 0) {
			categories.deleteCharAt(categories.length() - 1);
		}
		return categories.toString();
	}

	private void initReport(int position) {
		report = reports.getReports(this);

		if (report != null) {
			reportId = (int) report.get(position).getId();

			// fetch categories
			view.setBody(report.get(position).getDesc());
			view.setCategory(fetchCategories(reportId));
			view.setLocation(report.get(position).getLocation());
			view.setDate(report.get(position).getDate());
			view.setTitle(report.get(position).getTitle());
			view.setStatus(report.get(position).getStatus());
			view.setListNews((int) reportId);
			view.setListPhotos((int) reportId);
			view.setListVideos((int) reportId);
			view.getListPhotos().setOnItemClickListener(
					new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							Intent i = new Intent(ViewReportActivity.this,
									ViewReportPhotoActivity.class);
							i.putExtra("reportid", reportId);
							i.putExtra("position", position);
							startActivityForResult(i, 0);
							overridePendingTransition(R.anim.home_enter,
									R.anim.home_exit);
						}
					});
			view.getListNews().setOnItemClickListener(
					new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent i = new Intent(ViewReportActivity.this,
									ViewReportNewsActivity.class);
							i.putExtra("reportid", reportId);
							i.putExtra("position", position);
							startActivityForResult(i, 0);
							overridePendingTransition(R.anim.home_enter,
									R.anim.home_exit);
						}
					});

			view.getListVideos().setOnItemClickListener(
					new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent i = new Intent(ViewReportActivity.this,
									ViewReportVideoActivity.class);
							i.putExtra("reportid", reportId);
							i.putExtra("position", position);
							startActivityForResult(i, 0);
							overridePendingTransition(R.anim.home_enter,
									R.anim.home_exit);
						}
					});

			centerLocationWithMarker(getPoint(
					Double.parseDouble(report.get(position).getLatitude()),
					Double.parseDouble(report.get(position).getLongitude())));
			int page = position;
			this.setTitle(page + 1);
		}
		// animate views
		view.showViews();
	}

	public void setTitle(int page) {
		final StringBuilder title = new StringBuilder(String.valueOf(page));
		title.append("/");
		if (report != null)
			title.append(report.size());
		setActionBarTitle(title.toString());
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}