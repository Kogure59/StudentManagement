# RaiseTech Javaコース
## 実践概要
### プロジェクト(StudentManagement) 
Spring Boot・MyBatis・MySQL を用いた **受講生管理システム** を構築しました。  
受講生情報(氏名・メールアドレス・地域など)、受講生に紐づくコース情報(コース名・受講開始日・受講終了日)、  
および申込状況(仮申込・本申込・受講中など)を管理するアプリケーションです。  
バックエンド開発の基本構造理解、データベース連携、テストコード実装、AWSによるクラウド環境でのデプロイまで、一貫した開発プロセスを経験しています。

### 使用技術
- **バックエンド**：Spring Boot / MyBatis  
- **データベース**：MySQL（RDS）  
- **テスト**：JUnit5 / Mockito  
- **設計**：ER図 / テーブル設計 / レイヤードアーキテクチャ  
- **インフラ**：AWS（EC2 / RDS / ELB）、GitHub Actions（CI/CD）  
- **構成管理**：Git / GitHub / Gradle  

### 実践内容
#### 🟦 初級編
- Java文法（基本型、分岐、繰り返し、例外処理）
- StreamAPI / 正規表現
- オブジェクト指向の理解
- IntelliJの活用 / デバッグ手法
- Git / GitHub の基本操作

#### 🟨 中級編
- Webアプリケーションの仕組みの理解
- Spring Boot プロジェクト構築とMVCモデルの習得
- MyBatis を用いた CRUD 実装
- テーブル設計・ER図の作成
- Thymeleaf による画面描画
- REST API化（Read / Create）
- 入力チェック・例外処理の実装
- レイヤードアーキテクチャに基づき、Controller / Service / Repository / Converter の各レイヤーに責務を分離
- JUnit5 + Mockito によるテスト作成（Controller / Service / Repository）
- 条件指定検索・申込状況機能の追加

#### 🟥 上級編（クラウド × 自動化）
- AWSアカウント作成と IAM・料金管理の基礎理解
- **EC2** によるアプリケーションサーバー構築
- **RDS（MySQL）** を使用した本番環境データベースの構築
- セキュリティグループ・キーペア・VPC などのネットワーク設計
- EC2 上での Spring Boot アプリ実行
- **ELB（ロードバランサー）** を使った冗長化環境の構築
- Docker ハンズオン（コンテナ基礎）
- デプロイの考え方・注意点
- **GitHub Actions を用いた CI/CD パイプライン構築**  
  - レポジトリへの push --> 自動テスト  
  - Gradle による自動ビルド  
  - scp ＋ systemd による EC2 への自動デプロイ  
  - --> **main ブランチにマージすると EC2 のアプリが自動更新される状態を実現**

---

## CI/CD（GitHub Actions を用いた自動デプロイ）
このプロジェクトでは、GitHub Actions を使用した EC2 への自動デプロイ（CD） を構築しています。
main ブランチに変更を push または PR がマージされると、EC2 上のアプリケーションが自動的に更新されます。

ワークフロー定義ファイル：
[.github/workflows/JavaTest.yml](./.github/workflows/JavaTest.yml)

【自動処理の流れ】  
main ブランチへの push / PR で起動  
--> JDK 21（Temurin）環境のセットアップ  
--> Gradle Wrapper によるテスト (./gradlew test) 実行  
--> Gradle によるアプリケーションビルド (./gradlew bootJar)  
--> 生成された JAR ファイルを EC2 へ SCP で転送  
--> EC2 に SSH 接続し、systemctl によるサービス再起動  
--> StudentManagement.service が稼働している場合は restart  
--> 稼働していない場合は start

EC2 側では systemd（StudentManagement.service）によりアプリケーションを常駐管理しています。

---

## ER図
<img width="1887" height="581" alt="ER図" src="https://github.com/user-attachments/assets/f4f46cba-dfd9-4e8a-bcd8-f66edd131cd6" />

---

<details>
<summary>学習記録・カリキュラム</summary>

| No. | タイトル | カテゴリ | 必須課題 | 課題完了 |
| :-: | :------- | :------: | :-: | :-: |
| 1 | Javaの歴史 意識してほしいこと_AI活用方法 | 🟦初級編 | ☐ | - |
| 2 | 開発環境構築 | 🟦初級編 | ☐ | ☑ |
| 3 | HelloWorldの解説 | 🟦初級編 | ☐ | ☑ |
| 4 | 変数と値の取り扱い 簡単な計算 | 🟦初級編 | ☐ | ☑ |
| 5 | 基本型_標準API _分岐処理 | 🟦初級編 | ☐ | ☑ |
| 6 | 繰り返し処理_配列_ListとMap_switch式 | 🟦初級編 | ☐ | ☑ |
| 7 | StreamAPIとラムダ式 | 🟦初級編 | ☐ | ☑ |
| 8 | 入出力処理と例外処理 | 🟦初級編 | ☐ | ☑ |
| 9 | オブジェクト指向について | 🟦初級編 | ☐ | ☑ |
| 10 | 正規表現 | 🟦初級編 | ☐ | ☑ |
| 11 | Intellijの便利機能 | 🟦初級編 | ☐ | ☑ |
| 12 | Javaの命名規則と学習方法 | 🟦初級編 | ☐ | ☑ |
| 13 | デバッグの実践 | 🟦初級編 | ☐ | ☑ |
| - | 初級理解度チェック | 🟦初級編 | ☑ | ☑ |
| 14 | Webアプリの仕組み | 🟨中級編 | ☐ | - |
| 15 | SpringとSpringBootの違い | 🟨中級編 | ☐ | ☑ |
| 16 | バージョン管理とGitとGitHub | 🟨中級編 | ☑ | ☑ |
| 17 | プロジェクトの構成管理_Gradle_Maven | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/1 |
| 18 | DBを使わないWebアプリケーション構築 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/2 |
| 19 | DB_トランザクション_MySQL | 🟨中級編 | ☐ | ☑ |
| 20 | JDBCとMyBatis | 🟨中級編 | ☐ | ☑ |
| 21 | 実際に構築するWebアプリの解説とテーブル設計 | 🟨中級編 | ☐ | ☑ |
| 22 | モデル設計_MVCとMVVM_DBマイグレーション | 🟨中級編 | ☐ | - |
| 23 | MyBatisを使ってCRUDのRead処理を実装 | 🟨中級編 | ☑ | ☑ https://github.com/Kogure59/StudentManagement/pull/5 |
| 24 | Read処理のServiceとController部分を実装 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/6 |
| 25 | Read処理のConverter部分実装 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/7 |
| 26 | タスクの見積もり方 | 🟨中級編 | ☐ | - |
| 27 | Thymeleafを使ったReadの画面描画処理 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/8 |
| 28 | Thymeleafを使ったPOST処理 | 🟨中級編 | ☐ | ☑ |
| 29 | 受講生情報登録処理の実装 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/9 |
| 30 | 受講生情報更新処理の実装 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/10 |
| 31 | 受講生情報削除処理の実装 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/11 |
| 32 | REST_APIの解説 | 🟨中級編 | ☐ | - |
| 33 | Read処理のREST化とPostman実践 | 🟨中級編 | ☑ | ☑ https://github.com/Kogure59/StudentManagement/pull/12 |
| 34 | Create処理のREST化とリファクタリング | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/13 |
| 35 | 更新処理のリファクタリング_MapperXMLの導入_入力チェック | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/14 https://github.com/Kogure59/StudentManagement/pull/15 |
| 36 | SpringBootでの例外処理 | 🟨中級編 | ☑ | ☑ https://github.com/Kogure59/StudentManagement/pull/16 |
| 37 | 開発の流れと手法_開発プロセスのフレームワーク | 🟨中級編 | ☐ | - |
| 38 | ドキュメントの必要性と作り方 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/17 |
| 39 | テストとテスト手法について | 🟨中級編 | ☐ | ☑ |
| 40 | JUnitとAssertionとMockito_Serviceのテスト | 🟨中級編 | ☑ | ☑ https://github.com/Kogure59/StudentManagement/pull/18 |
| 41 | Controllerのテストと入力チェックのテスト | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/19 |
| 42 | Controllerのテスト解説とJUnitの機能解説 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/20 |
| 43 | RepositoryのテストとConverterのテスト解説 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/22 |
| 44 | 検索条件の追加と申込み状況機能の追加 | 🟨中級編 | ☑ | ☑ https://github.com/Kogure59/StudentManagement/pull/24 https://github.com/Kogure59/StudentManagement/pull/23 |
| 45 | クラウドとは_AWSアカウントの作成 | 🟥上級編 | ☐ | ☑ |
| 46 | EC2の構築 | 🟥上級編 | ☐ | ☑ |
| 47 | RDSの構築 | 🟥上級編 | ☐ | ☑ |
| 48 | AWS上でアプリケーションの動作確認 | 🟥上級編 | ☐ | ☑ |
| 49 | ELBの構築 | 🟥上級編 | ☐ | ☑ |
| 50 | 複雑なWebアプリケーション開発 | 🟥上級編 | ☐ | - |
| 51 | Dockerの解説とハンズオン | 🟥上級編 | ☐ | ☑ |
| 52 | デプロイの解説_注意点 | 🟥上級編 | ☐ | ☑ |
| 53 | CI:CDの解説_GitHubActionsの設定 | 🟥上級編 | ☐ | ☑ |
| 54 | CDの解説と設定 | 🟥上級編 | ☐ | ☑ |

</details>
