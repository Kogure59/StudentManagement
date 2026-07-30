# 受講生管理システム

Spring Boot・MyBatis・MySQL を用いて開発した受講生管理システムです。  
RaiseTech Java コースの課題として、受講生情報および受講コース情報を管理する Web アプリケーションを構築しました。  
REST API の設計・実装から、JUnit5 / Mockito によるテスト、AWS へのデプロイ、GitHub Actions を用いた CI/CD まで一貫して取り組みました。

## 機能一覧

- **受講生管理**：受講生情報の登録・一覧検索（条件指定検索に対応）・更新・論理削除（更新 API を通じて `isDeleted` フラグを切り替え）
- **コース管理**：受講生に紐づくコース情報の登録・全件検索・条件検索（コース名・受講期間による絞り込み）・更新
- **申込状況管理**：コースの申込状況を「仮申込」として初期登録・更新・専用 API で「本申込」へ状態遷移

## API 一覧

springdoc-openapi による Swagger UI（`/swagger-ui/index.html`）でも同様の情報を確認できますが、REST API の全体像を一目で把握できるよう一覧化しています。

### 受講生管理

| Method | Path | 概要 | リクエスト | レスポンス |
|---|---|---|---|---|
| GET | `/studentList` | 受講生詳細の条件指定検索（条件未指定時は全件検索） | クエリパラメータ：`name`, `kanaName`, `nickname`, `email`, `area`, `age`, `gender`, `remark`, `isDeleted`, `courseName`, `courseStartAt`, `courseEndAt`（すべて任意） | `StudentDetail` の配列 |
| GET | `/student/{id}` | 受講生詳細検索 | パスパラメータ：`id` | `StudentDetail` |
| POST | `/registerStudent` | 受講生登録（コース情報を含む） | リクエストボディ：`StudentDetail` | 登録した `StudentDetail` |
| PUT | `/updateStudent` | 受講生更新（コース情報を含む、`isDeleted` フラグの切り替え＝論理削除も同 API） | リクエストボディ：`StudentDetail` | 実行結果メッセージ |

### 申込状況管理

※ `/studentCourses/{studentCourseId}` 配下の API は、Path 列では共通部分を省略し、
`...` を `/studentCourses/{studentCourseId}` として表記しています。

| Method | Path | 概要 | リクエスト | レスポンス |
|---|---|---|---|---|
| GET | `/enrollmentStatuses` | 申込状況一覧検索（全件） | なし | `EnrollmentStatus` の配列 |
| GET | `/enrollmentStatuses/{id}` | ID による申込状況検索 | パスパラメータ：`id` | `EnrollmentStatus` |
| GET | `.../enrollmentStatus` | 受講生コース ID による申込状況検索 | パスパラメータ：`studentCourseId` | `EnrollmentStatus` |
| POST | `.../enrollmentStatus/initial` | 申込状況初期登録（仮申込） | パスパラメータ：`studentCourseId` | 登録した `EnrollmentStatus` |
| PUT | `.../enrollmentStatus` | 申込状況更新 | パスパラメータ：`studentCourseId`、リクエストボディ：`EnrollmentStatus` | 実行結果メッセージ |
| PUT | `.../enrollmentStatus/formal` | 本申込への昇格（仮申込 → 本申込） | パスパラメータ：`studentCourseId` | 更新後の `EnrollmentStatus` |

### その他

| Method | Path | 概要 | リクエスト | レスポンス |
|---|---|---|---|---|
| GET | `/exception` | `NotFoundException` によるエラーハンドリングの動作確認用エンドポイント | なし | 400 Bad Request + エラーメッセージ |

## 使用技術

- **バックエンド**：Spring Boot / MyBatis
- **データベース**：MySQL
- **テスト**：JUnit5 / Mockito
- **インフラ**：AWS（EC2 / RDS / ALB）
- **CI/CD**：GitHub Actions
- **開発ツール**：Git / GitHub / Gradle / Claude Code（コードレビュー・ドキュメント整備支援）

## システム構成
<img width="1078" height="731" alt="システム構成図 " src="https://github.com/user-attachments/assets/809040aa-69f0-400a-b39b-872d3e129a68" />
学習当時の AWS 環境における構成は以下の通りです。

- **VPC / サブネット構成**：ap-northeast-1a・1c の 2 つの AZ にまたがる VPC 作成時に作成された Public / Private サブネットを利用しています。実際にリソースを配置しているのは 1a のみで、1c は冗長化のために予約したのみで未使用です。
- **ALB / インターネットゲートウェイ**：ユーザーからのリクエストはインターネットゲートウェイ経由で ALB に到達し、ALB はターゲットグループに登録された Public サブネット（1a）上の EC2 へルーティングします。
- **GitHub Actions**：main ブランチへの push / PR マージをトリガーに、テスト実行・ビルド・EC2 へのデプロイを自動化しています（詳細は後述の CI/CD セクションを参照してください）。
- **EC2**：Spring Boot アプリケーションを systemd 管理下で常駐稼働させています。
- **RDS（MySQL）**：EC2 上のアプリケーションから MyBatis 経由で接続しています。

※ 学習期間終了後、EC2 / RDS 環境は停止・削除済みです。上記は学習当時の構成を示しています。

## ER図
<img width="1887" height="581" alt="ER図" src="https://github.com/user-attachments/assets/f4f46cba-dfd9-4e8a-bcd8-f66edd131cd6" />

## アーキテクチャ・設計

- Controller → Service → Repository（MyBatis）→ mapper XML というレイヤードアーキテクチャで責務を分離しています。
- Service 層に業務ロジックを集約し、Controller はリクエスト・レスポンスの制御に専念する構成としています。
- Repository 層は `@Mapper` インターフェースのみで SQL を持たず、実際のクエリは `mapper/*.xml` に分離しています。
- `StudentDetail`（`Student` + `StudentCourse` の集約 DTO）を `StudentConverter` が組み立て、API レスポンスとして返却しています。

## 工夫した点

### StudentConverter による集約設計
`Student` と `StudentCourse` は SQL の JOIN ではなく、アプリケーション層の `StudentConverter.convertStudentDetails` で `studentId` をキーにメモリ上で結合し、API 向けの `StudentDetail` を組み立てています。

### コース条件検索の実装
`searchStudentCourseByCondition` は MyBatis の `<if>` タグによる動的 WHERE 句でコース名・受講期間による絞り込みを実現しています。`StudentService` 側では、受講生条件とコース条件を別々に検索した上で、該当する受講生 ID の集合で結果を絞り込む方式を採用しています。

### 論理削除
`Student.isDeleted` フラグで管理し、削除専用の API・SQL は持たず、通常の更新 API を通じてフラグを切り替える設計としました。
データを物理削除せず保持することで、誤削除への対応や履歴管理を考慮しました。

### 申込状況の状態遷移
`EnrollmentStatus` は「仮申込」状態で初期登録する専用 API と、「本申込」へ昇格させる専用 API を分離しており、通常の更新処理とは独立した昇格フローを持ちます。

## テスト

JUnit5 / Mockito を用いて、Controller・Service・Repository・Converter の各レイヤーにテストを実装しています（受講生・申込状況の両リソースに対応）。

- Service：Mockito による依存モック化
- Controller：MockMvc を用いた API テスト
- Repository：MyBatis を含む DB アクセステスト
- Converter：集約ロジックの単体テスト

## セットアップ手順

### 前提
- Java 21
- MySQL（`StudentManagement` データベースを作成しておく必要があります。`src/main/resources` にはスキーマの自動初期化設定がないため、テーブルは事前に作成してください。）

### 環境変数
`application.properties` の DB 接続情報は環境変数から読み込みます。起動前にターミナルで以下を実行してください。

```
export DB_USERNAME="your_username"
export DB_PASSWORD="your_password"
```

### 起動

```
./gradlew bootRun
```

## CI/CD（GitHub Actions を用いた自動デプロイ）

このプロジェクトでは、GitHub Actions を使用した EC2 への自動デプロイ（CD）を構築しています。
main ブランチに変更を push または PR がマージされると、EC2 上のアプリケーションが自動的に更新されます。

ワークフロー定義ファイル：
[.github/workflows/JavaTest.yml](./.github/workflows/JavaTest.yml)

【自動処理の流れ】
- main ブランチへの push / PR で起動
- JDK 21（Temurin）環境のセットアップ
- Gradle Wrapper によるテスト（`./gradlew test`）実行
- Gradle によるアプリケーションビルド（`./gradlew bootJar`）
- 生成された JAR ファイルを EC2 へ SCP で転送
- EC2 に SSH 接続し、`systemctl` によるサービス再起動
- `StudentManagement.service` が稼働している場合は `restart`
- 稼働していない場合は `start`

EC2 側では systemd（`StudentManagement.service`）によりアプリケーションを常駐管理しています。

※ 現在の実行状況について
本リポジトリでは GitHub Actions による自動テスト・自動デプロイの設定を実装しています。  
当時使用していた AWS（EC2 / RDS）環境は学習期間終了後に停止・削除しているため、現在はデプロイ先が存在せず CD ジョブは失敗しますが、設定自体は学習時に正常動作を確認済みです。

## 開発の経緯

本プロジェクトは当初 Thymeleaf を用いたサーバーサイドレンダリングの Web アプリケーションとして開発を始め、
学習の進行に伴い REST API 化・Postman での動作検証を経て現在の構成に至りました。
開発の詳細な過程は [curriculum.md](./curriculum.md) に Pull Request リンク付きでまとめています。
