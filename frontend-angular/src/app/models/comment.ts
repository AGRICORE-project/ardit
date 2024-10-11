export interface Comment {
    id: number;
    createdAt: Date,
    lastUpdate: Date,
    deleted: boolean,
    userId: string,
    content: string,
    parentId: number,
    rootId: number,
    level: number,
    replies: Comment[]
}