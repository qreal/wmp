namespace java com.qreal.wmp.thrift.gen

include "../struct/Diagram.thrift"
include "../struct/Palette.thrift"

service EditorServiceThrift {
    i64 saveDiagram(1: Diagram.TDiagram diagram),
    Diagram.TDiagram openDiagram(1: i64 diagramID),
    void deleteDiagram(1: i64 id),
    void rewriteDiagram(1: Diagram.TDiagram diagram),
    i64 createFolder(1: Diagram.TFolder folder),
    void deleteFolder(1: i64 id),
    Diagram.TFolder getFolderTree(),
    i64 createPalette(1: Palette.TPalette palette)
}
