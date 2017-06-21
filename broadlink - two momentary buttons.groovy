preferences {    
	section("Internal Access"){
		input "id_pw", "text", title: "ID & PW for RM Plugin or RM Bridge(Optional)", required: false
		input "internal_ip", "text", title: "IP for RM Plugin or RM Bridge or HA(Required for all)", required: true
		input "internal_port", "text", title: "Port(Required for all)", required: true
		input "internal_on_path", "text", title: "On Path(Required for all)", required: true
        input "internal_off_path", "text", title: "Off Path", required: false
	}
}

metadata {
	definition (name: "Broadlink: Momentary Button", namespace: "btrial", author: "btrial") {
		capability "Actuator"
		capability "Switch"
		capability "Momentary"
		capability "Sensor"
        
		attribute "about", "string"
	}

	// simulator metadata
	simulator {
	}
	
	tiles(scale: 2) {
		multiAttributeTile(name: "main", type: "generic", width: 6, height: 4, canChangeIcon: true, canChangeBackground: true) {
			tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "off", label: 'Turning OFF', action: "switch.on", backgroundColor: "#ffffff", nextState: "undefined"
				attributeState "on", label: 'Turning ON', backgroundColor: "#79b821"
                attributeState "undefined", label: 'Push for ON', action: "switch.on", backgroundColor: "#ffffff"
			}
        }
        multiAttributeTile(name: "on", type: "generic", width: 6, height: 4, canChangeIcon: true, canChangeBackground: true) {
			tileAttribute("device.switchON", key: "PRIMARY_CONTROL") {
				attributeState "off", label: '', action: "switch.on", backgroundColor: "#ffffff",icon: "https://raw.githubusercontent.com/btrial/broadlink-smartthings/master/icons/push-for-ON.png", nextState: "on"
				attributeState "on", label: '', backgroundColor: "#79b821",icon: "https://raw.githubusercontent.com/btrial/broadlink-smartthings/master/icons/turning-ON.png"
			}
        }
        multiAttributeTile(name: "off", type: "generic", width: 6, height: 4, canChangeIcon: true, canChangeBackground: true) {
			tileAttribute("device.switchOFF", key: "PRIMARY_CONTROL") {
				attributeState "off", label: '', action: "switch.off", backgroundColor: "#ffffff",icon: "https://raw.githubusercontent.com/btrial/broadlink-smartthings/master/icons/push-for-OFF.png", nextState: "on"
				attributeState "on", label: '', backgroundColor: "#F75454",icon: "https://raw.githubusercontent.com/btrial/broadlink-smartthings/master/icons/turning-OFF.png"
			}
        }
        main "main"
		details (["on", "off"])
	}
}

def push() {
	push(internal_on_path)
}

def push(String path) {

    if (path){
    	def userpass = "Basic " + id_pw.encodeAsBase64().toString()
		def result = new physicalgraph.device.HubAction(
				method: "POST",		/* If you want to use the RM Bridge, change the method from "POST" to "Get" */
				path: "${path}",
				headers: [HOST: "${internal_ip}:${internal_port}", AUTHORIZATION: "${userpass}"],
                body: ["entity_id":"${body_data_for_ha}"]
				)
			sendHubCommand(result)
			log.debug result
	}
}

def on() {
	
    push(internal_on_path)

    sendEvent(name: "switchON", value: "on", isStateChange: true, display: false)
    sendEvent(name: "switchON", value: "off", isStateChange: true, display: false)
	
    sendEvent(name: "switch", value: "on", isStateChange: true, display: false)
    sendEvent(name: "switch", value: "undefined", isStateChange: true, display: false)
	sendEvent(name: "momentary", value: "pushed", isStateChange: true)
}

def off() {
	
    push(internal_off_path)
	
	sendEvent(name: "switchOFF", value: "on", isStateChange: true, display: false)
	sendEvent(name: "switchOFF", value: "off", isStateChange: true, display: false)
    
	sendEvent(name: "switch", value: "off", isStateChange: true, display: false)
    sendEvent(name: "switch", value: "undefined", isStateChange: true, display: false)
    sendEvent(name: "momentary", value: "pushed", isStateChange: true)
}