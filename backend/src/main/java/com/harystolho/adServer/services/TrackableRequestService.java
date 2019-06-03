package com.harystolho.adserver.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adserver.tracker.Tracker;
import com.harystolho.adserver.tracker.UserTrackerService;

@Service
public class TrackableRequestService {

	private UserTrackerService userTrackerService;

	public TrackableRequestService(UserTrackerService userTrackerService) {
		this.userTrackerService = userTrackerService;
	}

	
	/**
	 * Adds a tracker the the request if it doesn't contain one
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public Tracker addTrackerToRequest(HttpServletRequest req, HttpServletResponse res) {
		Tracker tracker = new Tracker(AEUtils.getCookieByName(req.getCookies(), UserTrackerService.COOKIE_NAME),
				req.getRemoteAddr());

		if (!userTrackerService.isTrackerValid(tracker)) {
			tracker = userTrackerService.createTracker(req.getRemoteAddr());
			res.addCookie(tracker.getCookie());
		}

		return tracker;
	}

}
