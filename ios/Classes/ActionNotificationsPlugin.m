#import "ActionNotificationsPlugin.h"
#if __has_include(<action_notifications/action_notifications-Swift.h>)
#import <action_notifications/action_notifications-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "action_notifications-Swift.h"
#endif

@implementation ActionNotificationsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftActionNotificationsPlugin registerWithRegistrar:registrar];
}
@end
