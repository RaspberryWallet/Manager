package io.raspberrywallet.mock

import io.raspberrywallet.contract.Manager
import io.raspberrywallet.contract.Response
import io.raspberrywallet.contract.WalletStatus
import io.raspberrywallet.contract.module.Module
import io.raspberrywallet.contract.module.ModuleState
import io.raspberrywallet.contract.step.SimpleStep
import java.io.File
import java.util.function.DoubleConsumer
import java.util.function.IntConsumer
import java.util.stream.Collectors.toMap

class ManagerMock : Manager {
    override fun addBlockChainProgressListener(listener: DoubleConsumer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAutoLockChannelListener(listener: IntConsumer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uploadNewModule(inputFile: File?, filename: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadWalletFromDisk(moduleToInputsMap: MutableMap<String, MutableMap<String, String>>) {
        TODO("not implemented")
    }

    override fun setDatabasePassword(password: String) {
        TODO("not implemented")
    }

    override fun getWalletStatus() = WalletStatus.ENCRYPTED

    override fun tap() {
        TODO("not implemented") 
    }

    override fun lockWallet(): Boolean {
        TODO("not implemented") 
    }

    override fun sendCoins(amount: String, recipientAddress: String) {
        TODO("not implemented") 
    }


    class SampleModule(name: String, description: String, val htmlUiForm: String? = null) : Module(name, description, null)

    private val _modules = listOf(
        SampleModule("PIN", "Module that require enter 4 digits code", """<input type="text" name="pin">"""),
        SampleModule("Button", "Module that require to push the button"),
        SampleModule("Server", "Module that require to authenticate with external server"),
        SampleModule("Google Authenticator", "Module that require to enter google auth code", """<input type="text" name="code">"""))
        .stream().collect(toMap(SampleModule::getId) { it })!!


    override fun ping() = "pong"

    override fun getServerModules() = _modules.values.toList()

    override fun getModuleState(moduleId: String): ModuleState {
        val randomModuleState = ModuleState.READY
        randomModuleState.message = "Waiting for user interaction"
        return randomModuleState
    }

    override fun nextStep(moduleId: String, input: Map<String, String>): Response =
        Response(SimpleStep("Do something"), Response.Status.OK)

    override fun unlockWallet(moduleToInputsMap: MutableMap<String, out MutableMap<String, String>>) {}

    override fun getCurrentReceiveAddress() = "1BoatSLRHtKNngkdXEeobR76b53LETtpyT"

    override fun getFreshReceiveAddress() = "1BoatSLRHtKNngkdXEeobR76b53LETtpyT"

    override fun getEstimatedBalance() = "0.0"

    override fun getAvailableBalance() = "0.0"

    override fun restoreFromBackupPhrase(mnemonicWords: MutableList<String>, selectedModulesWithInputs: MutableMap<String, MutableMap<String, String>>, required: Int) {
        val phrase = mnemonicWords.reduce { acc, s -> acc + s }
        println(phrase)
    }

    override fun getCpuTemperature() = "75 °C"

    override fun getNetworkList(): Array<String> = arrayOf<String>("UPCwifi", "other wifi", "klocuch12")

    override fun getWifiStatus(): MutableMap<String, String> = mutableMapOf("freq" to "21.37 GHz", "speed" to "21.37 Tb/s")

    override fun getWifiConfig(): MutableMap<String, String> = mutableMapOf("ssid" to "fakenet")

    override fun setWifiConfig(newConf: MutableMap<String, String>?): Int = 0
}
