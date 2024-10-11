export interface CommentDTO {
    id: number;
    content: string,
    parentId: number,
    rootId: number,
    datasetId: number
}