package org.example.events

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.utils.FileUpload
import java.awt.Color
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path

class SlashCommandInteractionEvent : ListenerAdapter() {
    override fun onSlashCommandInteraction(e: SlashCommandInteractionEvent) {
        val unknownMessage = "不明なコマンドです"
        when (e.name) {
            "test" -> testCommand(e)
            "stop" -> stopCommand(e)
            "qrinvitation" -> qrInvitation(e)
            else -> e.reply(unknownMessage).queue()
        }
    }

    private fun testCommand(e: SlashCommandInteractionEvent) {
        val message = "テスト"
        e.reply(message).queue()
    }

    private fun stopCommand(e: SlashCommandInteractionEvent) {
        val member = e.member
        if (member?.isOwner == true) {
            val message = "シャットダウンします"
            e.reply(message).queue()
            val jda = e.jda
            jda.shutdown()
        } else {
            val message = "権限が足りません"
            e.reply(message).queue()
        }
    }

    private fun qrInvitation(e: SlashCommandInteractionEvent) {
        val member = e.member

        if (member?.hasPermission(Permission.CREATE_INSTANT_INVITE) == true) {
            val qrCodePath = "./QRCode.png"
            val channel = e.messageChannel // メッセージが送信されたチャンネルを取得

            if (channel !is TextChannel) {
                val message = "このチャンネルでURLを招待URLを発行することはできません"
                e.reply(message).queue()
                return // テキストチャットではなかったらそもそも実行しない
            }

            val invitation = channel.createInvite().setMaxAge(0).complete() // 招待リンクを即座に作成
            val invitationURL = invitation.url
            if (makeQR(invitationURL, qrCodePath)) {
                val message = "招待URLを発行しました\n $invitationURL"
                val qrCode = File(qrCodePath)
                e.reply(message).addFiles(FileUpload.fromData(qrCode)).queue()
            } else {
                val message = "QR生成中にエラーが発生しました"
                e.reply(message).queue()
            }
        } else {
            val message = "権限が足りません"
            e.reply(message).queue()
        }
    }

    private fun makeQR(url:String, filePath:String):Boolean {
        val qrCodeWriter = QRCodeWriter()
        val width = 350
        val height = 350

        try {
            // QRコードのビットマトリクスを生成
            val bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height)
            // QRコード画像をファイルに保存
            val path: Path = FileSystems.getDefault().getPath(filePath) // QRを保存する
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}