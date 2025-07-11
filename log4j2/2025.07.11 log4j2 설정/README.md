
```bash
$ gradle bootRun
```

## root로 전체 로깅 레벨을 설정

```yaml
logging:
  level:
    root: info
    org.study.runner.RunnerConfig: trace
```


## log4j2 설정 파일 읽는 순서

1. resources 디렉토리 아래에 log4j2.xml 파일이 있으면 해당 파일의 설정을 읽습니다.
2. log4j2.xml 파일이 없는 경우 .properties(.yml) 파일을 읽습니다.
3. 위 두 파일이 모두 있는 경우 log4j2.xml 을 적용시키고, .properties(.yml) 파일 설정을 덮습니다.


## 한글 깨짐 해결

- log4j2
```xml
<?xml version="1.0" encoding="UTF-8"?>
```


