import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.

  // platformChannel.setMethodCallHandler((call) async {
  //   print("Inside callback");
  //   print(call.toString());
  // });
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "Sample App",
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Sample App'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final platformChannel = MethodChannel("kotlin_channel");

  getValue() async {
    final phoneNumber = await platformChannel.invokeMethod("kotlin_method") as String;
    
    print("Incoming call from $phoneNumber from Flutter");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                "Make sure all permissions are granted for the App",
                style: TextStyle(
                    color: Colors.orange,
                    fontSize: 22,
                    fontWeight: FontWeight.bold),
                textAlign: TextAlign.center,
              ),
              Text(
                "Try calling this device from other device, you should see the incoming number in notification (also in logs)",
                style: TextStyle(
                    color: Colors.lightBlue,
                    fontSize: 20,
                    fontWeight: FontWeight.bold),
                textAlign: TextAlign.center,
              ),
              ElevatedButton(onPressed: getValue, child: Text("Push this"))
            ],
          ),
        ));
  }
}
