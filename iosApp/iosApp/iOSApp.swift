import SwiftUI
import shared

@main
struct iOSApp: App {
    private let sdk = SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory())
    
	var body: some Scene {
		WindowGroup {
            ContentView(viewModel: .init(sdk: sdk))
		}
	}
}
