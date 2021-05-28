//
//  RocketLaunchRow.swift
//  iosApp
//
//  Created by Nicolas Picon on 28/05/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct RocketLaunchRow: View {
    var rocketLaunch: RocketLaunch
    
    var body: some View {
        HStack() {
            VStack(alignment: .leading, spacing: 10.0, content: {
                Text("Launch name: \(rocketLaunch.missionName)")
                Text(launchText).foregroundColor(launchColor)
                Text("Launch year: \(String(rocketLaunch.launchYear))")
                Text("Launch details: \(rocketLaunch.details ?? "")")
            })
        }
    }
}

struct RocketLaunchRow_Previews: PreviewProvider {
    static var previews: some View {
        RocketLaunchRow(
            rocketLaunch: .init(
                flightNumber: 1,
                missionName: "Mission name",
                launchYear: 2021,
                launchDateUTC: "Date UTC",
                rocket: Rocket_.init(id: "Rocket ID", name: "Ariane", type: "Gros"),
                details: "Some details",
                launchSuccess: true,
                links: Links.init(missionPatchUrl: nil, articleUrl: nil)
            )
        )
    }
}


extension RocketLaunchRow {
   private var launchText: String {
       if let isSuccess = rocketLaunch.launchSuccess {
           return isSuccess.boolValue ? "Successful" : "Unsuccessful"
       } else {
           return "No data"
       }
   }

   private var launchColor: Color {
       if let isSuccess = rocketLaunch.launchSuccess {
           return isSuccess.boolValue ? Color.green : Color.red
       } else {
           return Color.gray
       }
   }
}
