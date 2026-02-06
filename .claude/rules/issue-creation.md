# Issue Creation Rules

## 이슈 제목 형식

```
<tag>: <description>
```

### 허용된 태그
- `feat` - 새로운 기능 추가
- `fix` - 버그 수정
- `refactor` - 코드 리팩토링
- `docs` - 문서 수정
- `chore` - 빌드, 패키지 관련
- `test` - 테스트 코드
- `style` - 코드 스타일 변경 (포맷팅 등)
- `perf` - 성능 개선
- `build` - 빌드 시스템 변경
- `ci` - CI 설정 변경
- `revert` - 커밋 되돌리기

### 예시
- ✅ `feat: University 조회 API 구현`
- ✅ `fix: 메모리 누수 문제 해결`
- ❌ `university api` (태그 없음)
- ❌ `feature: add login` (잘못된 태그)

## 이슈 본문 구조

이슈는 반드시 다음 3가지 섹션으로 구성됩니다:

### 1. 목적
구현하려는 기능의 목적과 배경을 간결하게 설명합니다.

```markdown
## 목적
University(대학교) 엔티티에 대한 전체 조회 및 상세 조회 API를 구현하여
클라이언트가 대학교 목록과 상세 정보를 조회할 수 있도록 합니다.
```

### 2. 구현 내용
구현할 기능과 요구사항을 구체적으로 나열합니다.

```markdown
## 구현 내용
- **전체 조회 API** (`GET /api/universities`)
  - 기본 정렬: nameEng 사전순
  - 필터링: isExchange, isVisit, nation
  - 검색: 학교명 (nameKor, nameEng)
- **상세 조회 API** (`GET /api/universities/{id}`)
  - University 모든 필드 출력
  - 연관된 LanguageRequirement 데이터 포함
```

### 3. 예상 구조
API 응답 구조나 데이터 구조를 JSON 예시로 명시합니다.

```markdown
## 예상 구조

**전체 조회 응답**
\`\`\`json
{
  "code": "200",
  "message": "Success",
  "result": [
    {
      "id": 1,
      "nameKor": "하버드 대학교",
      "nameEng": "Harvard University",
      "nation": "미국"
    }
  ]
}
\`\`\`
```

## 이슈 생성 템플릿

```markdown
## 목적
[기능의 목적과 배경을 1-2문장으로 설명]

## 구현 내용
- [구현할 기능 1]
- [구현할 기능 2]
- [기술적 요구사항]

## 예상 구조
[API 응답 구조나 데이터 모델을 JSON/코드 예시로 제시]
\`\`\`json
{
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "result": { ... }
}
\`\`\`
```

## 이슈 작성 시 주의사항

### ✅ DO
- 제목은 간결하고 명확하게 (60자 이내)
- 목적은 "왜" 이 기능이 필요한지 설명
- 구현 내용은 "무엇을" 구현할지 명확히 나열
- 예상 구조는 실제 JSON/코드 예시로 제시

### ❌ DON'T
- 이슈에 구현 상세(Step 1, Step 2 등)를 과도하게 작성하지 않음
- AS-IS/TO-BE 체크리스트 형식 지양 (간결하게 작성)
- 너무 많은 기술적 상세를 이슈에 포함하지 않음
- 한 이슈에 너무 많은 기능을 포함하지 않음 (단일 책임)

## 브랜치 네이밍

이슈 생성 후 작업 시 브랜치명은 다음 형식을 따릅니다:

```
<tag>/<이슈번호>
```

### 예시
- ✅ `feat/10`
- ✅ `fix/23`
- ❌ `feature/add-login`
- ❌ `feat-10`

## 커밋 메시지 형식

```
[#이슈번호]; <tag>: <description>
```

### 예시
- ✅ `[#10]; feat: University 조회 API 구현`
- ✅ `[#23]; fix: null pointer exception 해결`
- ❌ `feat: add university api` (이슈 번호 없음)
