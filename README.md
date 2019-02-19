# org.openhab.binding.izoneac
This is a SmartHome/openHAB binding for iZone Air Conditioning contoller.  iZone is one of the few popular third-party air conditioner controllers available in Australia for many air conditioner brands.  For more information about the product please go to https://izone.com.au/smart-airconditioning/.

To use this binding you must have the iZone's WiFi/Wired Home Automation Module (CHAM) installed.  i.e If you can control iZone AC via your mobile phone, then you have this hardware module installed.

# Features
This binding supports most functionality as the original iZone controller.
- View air conditioner information.
- Control air conditioner temperature.
- Set fan speed
- Set air conditioning mode
- Open/close each zone
- Set min/max airflow of each zone
- Activate sleep timer

This binding is created for automation via SmartHome/openHAB in mind.  Currently, activating Favourites or predefined Schedule are not supported.  Please request if you would like either feature supported.

# Prequisite
1. iZone air conditioning controller.
2. WiFi/Wired Home Automation Module (CHAM).
3. CHAM connected to your router via WiFi or LAN (only the newer version has LAN port).
4. Static address assigned to CHAM.

# Installation
For openHAB install "Eclipse IoT Market" add-on under MISC tab in openHAB Paper UI. Then install "iZone Air Conditioning Controller Binding" from the Bindings page.

For Eclipse SmartHome install from https://marketplace.eclipse.org/content/izone-air-conditioning-controller-binding

# Limitation
This release will require you assign a static address to the iZone Home Automation Module (CHAM).  This can be accomplished in two ways:

1. In iZone control panel go to "WiFi Configuration", select "Manual Configuration" and enter a static IP address like 192.168.1.30.
2. In the scenario where you do not have the password to iZone "System Configuration", assign a static IP address to the CHAM MAC address in your router.

# Configuration
Instruction below is for configuration under Paper UI.

1. Add iZone AC Controller.  Enter the nominated static IP address as outlined above in the Network Address field.  Leave Network Port as 80.  You may change to poll frequency to a value above 60 seconds.  30 seconds and under is not recomended.  Then choose the channels you want to to use.

2. Go to the Inbox, you should see all the zones available.  If not, go back to Configuration->Things and Add iZone Air Conditioning Controller Binding.  This should bring up the Inbox page.  Add the zone(s) you would like to control.

# Compatibility
Please note, this binding was created for iZone 310 but it should be compatible with newer models of the controller.  If there is an issue please log a defect.
