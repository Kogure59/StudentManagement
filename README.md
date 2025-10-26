# RaiseTech Javaコース
## 実践概要
### プロジェクト(StudentManagement) 
Spring Boot・MyBatis・MySQLを用いた**受講生管理システム**を構築しました。
バックエンド開発の基本構造理解から、データベース連携、テスト、機能拡張まで一貫して実装を行い、実務に近い開発プロセスを体験しました。
#### 使用技術
- **バックエンド**：Spring Boot / MyBatis  
- **データベース**：MySQL  
- **テスト**：JUnit5 / Mockito  
- **設計**：ER図 / テーブル設計  
- **インフラ（予定）**：AWS（EC2 / RDS / ELB など）
#### 実践内容
- **Spring Boot** によるWebアプリケーション開発の基本構造理解  
- **MyBatis** を用いたデータベース連携（CRUD処理）の実装  
- **テーブル設計・ER図作成** によるデータ構造の設計力強化  
- **Service・Controller・Repository・Converter層** の責務分離設計  
- **JUnit5 + Mockito** を使用したユニットテスト・リポジトリテストの実践  
- **REST API化** による外部連携の理解  
- **申込状況機能・検索機能の拡張** による応用的な機能実装

本システムを通じて、Webアプリケーションの開発プロセス全体（設計〜実装〜テスト）を一貫して学びました。
## 今後の展望（上級編への取り組み）※10月26日時点
これまでに **初級編・中級編** を通して、  
Spring Boot・MyBatis・MySQLを中心としたWebアプリケーション開発、およびテストコードや設計手法を体系的に学びました。  

今後は **上級編** に進み、AWSを活用した**クラウド環境でのデプロイ・運用**を学習予定です。

これにより、アプリケーション開発だけでなく、  
**クラウドインフラ構築から運用までを一貫して行うスキル**の習得を目指しています。

## ER図
<img width="1887" height="581" alt="ER図" src="https://github.com/user-attachments/assets/f4f46cba-dfd9-4e8a-bcd8-f66edd131cd6" />

## カリキュラム
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
| 39 | テストとテスト手法について | 🟨中級編 | ☐ | ☐ |
| 40 | JUnitとAssertionとMockito_Serviceのテスト | 🟨中級編 | ☑ | ☑ https://github.com/Kogure59/StudentManagement/pull/18 |
| 41 | Controllerのテストと入力チェックのテスト | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/19 |
| 42 | Controllerのテスト解説とJUnitの機能解説 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/20 |
| 43 | RepositoryのテストとConverterのテスト解説 | 🟨中級編 | ☐ | ☑ https://github.com/Kogure59/StudentManagement/pull/22 |
| 44 | 検索条件の追加と申込み状況機能の追加 | 🟨中級編 | ☑ | ☑ https://github.com/Kogure59/StudentManagement/pull/24 https://github.com/Kogure59/StudentManagement/pull/23 |
| 45 | クラウドとは_AWSアカウントの作成 | 🟥上級編 | ☐ | ☐ |
| 46 | EC2の構築 | 🟥上級編 | ☐ | ☐ |
| 47 | RDSの構築 | 🟥上級編 | ☐ | ☐ |
| 48 | AWS上でアプリケーションの動作確認 | 🟥上級編 | ☐ | ☐ |
| 49 | ELBの構築 | 🟥上級編 | ☐ | ☐ |
| 50 | 複雑なWebアプリケーション開発 | 🟥上級編 | ☐ | ☐ |
| 51 | Dockerの解説とハンズオン | 🟥上級編 | ☐ | ☐ |
| 52 | デプロイの解説_注意点 | 🟥上級編 | ☐ | ☐ |
| 53 | CI:CDの解説_GitHubActionsの設定 | 🟥上級編 | ☐ | ☐ |
| 54 | CDの解説と設定 | 🟥上級編 | ☐ | ☐ |
