# Issue Creation Guide

## 이슈 제목 형식

```
<tag>: <description>
```

### 허용된 태그
- `feat` - 새로운 기능 추가
- `fix` - 버그 수정
- `refactor` - 코드 리팩토링
- `docs` - 문서 수정
- `chore` - 빌드, 설정, 파이프라인 관련
- `test` - 테스트 코드
- `style` - 코드 스타일 변경 (포맷팅 등)
- `perf` - 성능 개선
- `build` - 빌드 시스템 변경
- `ci` - CI 설정 변경
- `revert` - 커밋 되돌리기

### 예시
- ✅ `feat: University 조회 API 구현`
- ✅ `chore: CD 파이프라인 임시 제거 및 별도 CI 파이프라인 추가`
- ✅ `fix: null pointer exception 해결`
- ❌ `university api` (태그 없음)
- ❌ `feature: add login` (잘못된 태그)

---

## 이슈 본문 구조

이슈는 다음 섹션으로 구성됩니다. **필수 섹션**은 반드시 포함하고, **옵션 섹션**은 필요에 따라 추가합니다.

### 필수 섹션

#### 1. 목적 (필수)
**구현하려는 기능의 목적과 배경을 1-2문장으로 간결하게 설명합니다.**

```markdown
## 목적
University(대학교) 엔티티에 대한 전체 조회 및 상세 조회 API를 구현하여
클라이언트가 대학교 목록과 상세 정보를 조회할 수 있도록 합니다.
```

#### 2. 구현 내용 (필수)
**구현할 기능과 요구사항을 구체적으로 나열합니다.**

**방법 A: 기능 나열 방식** (일반적인 신규 기능)
```markdown
## 구현 내용
- **전체 조회 API** (`GET /api/v1/universities`)
  - 기본 정렬: nameEng 사전순
  - 페이지네이션: pageInfo 포함
  - 동적 필터링: nation, isExchange, isVisit
  - 검색: search 파라미터로 학교명(nameKor, nameEng) 검색

- **상세 조회 API** (`GET /api/v1/universities/{id}`)
  - University 엔티티의 모든 필드 출력
  - 연관된 LanguageRequirement 데이터 포함
```

**방법 B: AS-IS/TO-BE 방식** (기존 코드 수정, 설정 변경)
```markdown
## 구현 내용

### AS-IS
- main 브랜치에 push하면 deploy.yaml 액션이 실행되어 배포 작업이 자동으로 실행됨
- CI/CD 파이프라인 작업이 매번 실행되어 시간이 오래 걸리고 ECR 이미지만 증가

### TO-BE
- [ ] deploy.yml: 수동 실행(workflow_dispatch)으로만 수정
- [ ] ci.yml: 모든 브랜치 푸시 또는 main PR 시 실행, 빌드/테스트 통과 여부만 확인
```

#### 3. 예상 구조 (필수)
**API 응답 구조나 데이터 모델을 JSON/코드 예시로 제시합니다.**

```markdown
## 예상 구조

**전체 조회 응답**
\`\`\`json
{
  "code": "200",
  "message": "Success",
  "result": {
    "universities": [
      {
        "id": 1,
        "nameKor": "콜로라도주립대 - 푸에블로 대학교",
        "nameEng": "Colorado State University - Pueblo",
        "nation": "미국",
        "region": "북미"
      }
    ],
    "pageInfo": {
      "currentPage": 0,
      "totalElements": 85,
      "totalPages": 9,
      "isLast": false
    }
  }
}
\`\`\`

**상세 조회 응답**
\`\`\`json
{
  "code": "200",
  "message": "Success",
  "result": {
    "id": 15,
    "nameKor": "라드바우드대",
    "nameEng": "Radboud University",
    "languageRequirements": [
      {
        "languageGroup": "영어",
        "examType": "TOEFL iBT",
        "minScore": 80.0
      }
    ]
  }
}
\`\`\`
```

### 옵션 섹션

#### 영향 범위 (옵션)
**변경사항이 영향을 미치는 컴포넌트나 시스템을 명시합니다.**
주로 `chore`, `fix`, `refactor` 이슈에서 유용합니다.

```markdown
## 영향 범위
- CI/CD 파이프라인
- GitHub Actions 워크플로우 (deploy.yml, ci.yml)
- EC2, ECR 등의 AWS 리소스
```

또는

```markdown
## 영향 범위

| 계층 | 영향 받는 컴포넌트 | 설명 |
|------|-------------------|------|
| **Storage** | `entity/UniversityEntity.kt` | 새로 생성 |
| **Database** | `university` 테이블 | 자동 생성 (JPA DDL) |
```

#### 특이사항 (옵션)
**구현 시 고려해야 할 사항, 주의점, 기술적 제약 등을 명시합니다.**

```markdown
## 특이사항
- anthropics/claude-code-action@beta는 Issue-only 리뷰를 지원하지 않음
- @v1 Action을 사용해야 이슈 발생 시 정상적인 응답 가능
- TOEIC의 경우 Listening/Reading과 Speaking/Writing을 모두 응시해야 함
```

#### 추후 구현 (옵션)
**현재 이슈 범위를 벗어나지만 향후 구현 예정인 기능을 명시합니다.**

```markdown
## 추후 구현
- Excel 파일 기반 학교 등록 (POST /api/v1/universities/upload)
  - 국제처 교환학생 Excel 파일을 입력하면 DB에 학교 데이터 저장
  - AWS S3 + Lambda + Python 파싱 파이프라인 구축
```

---

## 전체 템플릿

### 기본 템플릿 (신규 기능)
```markdown
## 목적
[기능의 목적과 배경을 1-2문장으로 설명]

## 구현 내용
- **기능 A** (`엔드포인트 또는 컴포넌트`)
  - 세부 요구사항 1
  - 세부 요구사항 2
- **기능 B**
  - 세부 요구사항

## 예상 구조
\`\`\`json
{
  "code": "200",
  "message": "Success",
  "result": { ... }
}
\`\`\`

## 추후 구현 (옵션)
- 향후 구현 예정 기능
```

### AS-IS/TO-BE 템플릿 (기존 코드 수정)
```markdown
## 목적
[변경의 목적과 배경]

## 영향 범위
- 영향 받는 시스템/컴포넌트 1
- 영향 받는 시스템/컴포넌트 2

## 구현 내용

### TO-BE
- [ ] 변경 사항 1
- [ ] 변경 사항 2

## 특이사항
- 주의할 점이나 제약사항
```

---

## 이슈 작성 가이드

### ✅ DO
- **제목은 간결하고 명확하게** (60자 이내)
- **목적은 "왜" 필요한지 설명** (배경과 목표)
- **구현 내용은 "무엇을" 구현할지 명확히 나열**
- **예상 구조는 실제 JSON/코드 예시로 제시**
- **필요시 옵션 섹션 추가** (영향 범위, 특이사항, 추후 구현)

### ❌ DON'T
- 이슈에 구현 상세(Step 1, Step 2 등)를 과도하게 작성하지 않음
- 한 이슈에 너무 많은 기능을 포함하지 않음 (단일 책임 원칙)
- 추상적이거나 모호한 표현 지양 (구체적으로 작성)
- AS-IS/TO-BE 체크리스트를 과도하게 상세히 작성하지 않음

---

## 브랜치 네이밍

이슈 생성 후 작업 시 브랜치명은 다음 형식을 따릅니다:

```
<tag>/<이슈번호>
```

### 예시
- ✅ `feat/10`
- ✅ `feat/11`
- ✅ `chore/6`
- ✅ `fix/23`
- ❌ `feature/add-login` (태그 오타, 이슈 번호 없음)
- ❌ `feat-10` (슬래시 누락)

---

## 커밋 메시지 형식

```
[#이슈번호]; <tag>: <description>
```

### 예시
- ✅ `[#10]; feat: University 조회 API 구현`
- ✅ `[#10]; feat: Repository 구현`
- ✅ `[#23]; fix: null pointer exception 해결`
- ✅ `[#6]; chore: CD 파이프라인 제거 및 CI 추가`
- ❌ `feat: add university api` (이슈 번호 없음)
- ❌ `#10 feat: add api` (형식 오류)

### 커밋 메시지 작성 가이드
- **하나의 이슈에 여러 커밋 가능** (기능 단위로 나눠서 커밋)
- **커밋 메시지는 구체적으로** (예: "Repository 구현", "Controller 구현")
- **불필요한 커밋 메시지 지양** (예: "코드 수정", "오류 해결" 등 추상적 표현)

---

## 실제 이슈 예시

### 예시 1: 신규 API 기능 (#10)
```markdown
## 목적
University(대학교) 엔티티에 대한 전체 조회 및 상세 조회 API를 구현하여
클라이언트가 대학교 목록과 상세 정보를 조회할 수 있도록 합니다.

## 구현 내용
1. 전체 조회 및 검색/필터링
   - Endpoint: GET /api/v1/universities
   - 기본 정렬: nameEng 사전순
   - 페이지네이션: pageInfo 포함 (currentPage, totalElements, totalPages, isLast)
   - 동적 필터링: nation (나라별), isExchange, isVisit
   - 검색: search 파라미터로 학교명(nameKor, nameEng) 검색

2. 상세 조회
   - Endpoint: GET /api/v1/universities/{id}
   - 정보 출력: University 엔티티의 모든 필드
   - 연관 조회: 해당 학교의 모든 LanguageRequirement 데이터 포함

## 예상 구조
(생략)
```

### 예시 2: 설정 변경 (#6)
```markdown
## 영향 범위
- CI/CD 파이프라인
- EC2, ECR 등의 AWS 리소스

## 구현 내용

### AS-IS
- main 브랜치에 push하면 deploy.yaml의 액션이 실행되어 배포 작업이 자동으로 실행됨
- 이로 인해 CI/CD 파이프라인 작업이 매번 실행되어 시간도 오래 걸리고 ECR에 이미지만 계속 생김

### TO-BE
- [ ] deploy.yml: 수동 실행(workflow_dispatch)으로만 수정
- [ ] ci.yml: 모든 브랜치에 푸시되거나 main으로 PR이 생성될 때 실행, 빌드와 테스트 통과 여부만 확인

## 특이사항
- 배포는 수동으로만 실행되도록 변경
```

### 예시 3: 추후 구현 포함 (#11)
```markdown
## 목적
숭실대학교 국제처에서 제공하는 교환방문학생 파견 가능 대학 및 지원 자격 파일
또는 학교 정보를 직접 입력하면 해당 학교가 등록되도록 한다.

## 구현 내용
- 직접 학교 등록 API (POST /api/v1/universities)
  - 학교 정보 데이터를 입력 받고 DB에 저장
  - DB 스키마에 맞게 입력 받도록 함

## 예상 구조
(생략)

## 추후 구현
- Excel 파일 기반 학교 등록 (POST /api/v1/universities/upload)
  - 국제처 교환학생 Excel 파일을 입력하면 DB에 해당 학교 데이터 저장
```
