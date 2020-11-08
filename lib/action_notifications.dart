import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

// TODO REFACTOR THIS MESS
class Priority {
  final _value;
  const Priority._internal(this._value);
  toString() => 'Priority.$_value';

  static const MAX = 2;
  static const HIGH = 1;
  static const DEFAULT = 0;
  static const LOW = -1;
  static const MIN = -2;
}

class ActionNotifications {
  static const MethodChannel _channel =
      const MethodChannel('action_notifications');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Uint8List getByteArray(ByteData data) {
    final buffer = data.buffer;
    return  buffer.asUint8List(data.offsetInBytes, data.lengthInBytes);
  }

  static void showSimpleNotification({
    String channelId,
    @required String smallIconName,
    @required String smallIconDefType,
    ByteData largeIconData,
    String notificationTitle,
    String notificationText,
    bool showWhen,
    @required bool shouldUseBigText,
    int priority = Priority.DEFAULT
  }) async {

    Uint8List largeIconByteData;
    if(largeIconData != null){
      largeIconByteData = getByteArray(largeIconData);
    }

    final data = await _channel.invokeMethod('showSimpleNotification', {
      "channelId": channelId,
      "smallIconName": smallIconName,
      "smallIconDefType": smallIconDefType,
      "largeIconByteData": largeIconByteData,
      "notificationTitle": notificationTitle,
      "notificationText": notificationText,
      "showWhen": showWhen,
      "shouldUseBigText": shouldUseBigText,
      "notificationPriority": priority
    });
    return data;
  }
}
