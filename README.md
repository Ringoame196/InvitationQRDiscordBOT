# DiscordBotKotlinTemplate
Kotlinでコマンドを実行できるDiscordBotを開発ができるテンプレート

## DiscordBot作成
 - [DiscordのDeveloper Portal](https://discord.com/developers/applications)でDiscordBot作成

## コマンド
 - Mainファイル13行目に 「コマンド名、コマンド説明」を追加する
 - SlashCommandInteractionEventファイル9行目に 「コマンド名 -> 処理」を追加することで コマンド実行時の処理を実装できる

## 開発
 - `bootBot` DiscordBOTを起動する
 - `build` jarファイルをビルドする

## 使い方
 - java -jar (jarファイル名).jarで起動
 - 初期起動時に生成されるtoken.txtファイルに、DiscordBotのTokenを貼り付け

## コマンド権限
鯖設定 -> 連携サービス -> BOT -> 設定したいコマンド -> 使用を許可する権限 チャンネルを設定
